import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.util.regex.Matcher
import java.util.regex.Pattern
/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = ""
poPackageName = ""
servicePackageName = ""
feignName = ""
typeMapping = [
        (~/(?i)tinyint|smallint|mediumint/)      : "Integer",
        (~/(?i)int/)                             : "Long",
        (~/(?i)bool|bit/)                        : "Boolean",
        (~/(?i)float|double|decimal|real/)       : "Double",
        (~/(?i)datetime|timestamp|date|time/)    : "Date",
        (~/(?i)blob|binary|bfile|clob|raw|image/): "InputStream",
        (~/(?i)/)                                : "String"
]


FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}


def generate(table, dir) {
    def tableName = table.getName();
    if(tableName.matches("t_.+")){
        tableName = tableName.substring(2)
    }
    def className = javaName(tableName, true) + "Feign"


    def poName = javaName(tableName, true) + "Po"
    def name = javaName(tableName, false)
    packageName = getPackageName(dir)
    feignName = getFeignName(dir);
    def domain = packageName.toString().replace(".api.feign;","").split("\\.")
    def moduleName = domain[domain.length-1]
    if(name.toString().startsWith(moduleName)){
        name = name.toString().substring(moduleName.length())
        name = name.substring(0,1).toLowerCase() +name.substring(1);
    }

    name = domain[domain.length-1]+"/"+name
    poPackageName = getPoPackageName(packageName)
    servicePackageName = getServicePackageName(packageName)
    new File(dir, className + ".java").withPrintWriter("utf-8") { out -> generate(out, className,feignName,poName, name) }
}
def getFeignName(dir){
    System.out.println(dir);
    def a = dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".");
    Pattern r = Pattern.compile("unit\\-[a-z]{1,}\\-api");
    Matcher m = r.matcher(a);
    def moduleName =""
    if (m.find()) {
        moduleName = m.group(0).replace("-api","");
    }
    return moduleName;
}
// 获取包所在文件夹路径
def getPackageName(dir) {
    return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}
def getPoPackageName(packageName){
    return packageName.toString().replace(".feign;",".po.");
}
def getServicePackageName(packageName){
    return packageName.toString().replace(".feign;",".service.");
}

def generate(out, className, feignName, poName, name) {
    out.println "package $packageName"
    out.println ""
    out.println "import com.goumang.core.web.WebResponse;"
    out.println "import org.springframework.cloud.openfeign.FeignClient;"
    out.println "import org.springframework.web.bind.annotation.PathVariable;"
    out.println "import org.springframework.web.bind.annotation.RequestBody;"
    out.println "import org.springframework.web.bind.annotation.RequestMapping;"
    out.println "import org.springframework.web.bind.annotation.RequestMethod;"
    out.println "import java.util.Map;"
    out.println ""
    out.println "@FeignClient(name=\"$feignName\")"
    out.println "@RequestMapping(\"/$name\")"
    out.println "public interface $className {"
    out.println ""
    out.println "    @RequestMapping(value = \"{id}\", method = RequestMethod.GET)"
    out.println "    WebResponse info(@PathVariable(\"id\") Long id);"
    out.println ""
    out.println "    @RequestMapping(value = \"/info\", method = RequestMethod.POST)"
    out.println "    WebResponse info(@RequestBody Map<String,Object> map);"
    out.println ""
    out.println "    @RequestMapping(value = \"/list\", method = RequestMethod.POST)"
    out.println "    WebResponse list(@RequestBody Map<String,Object> map);"
    out.println ""
    out.println "    @RequestMapping(value = \"/page\", method = RequestMethod.POST)"
    out.println "    WebResponse page(@RequestBody Map<String,Object> map);"
    out.println ""
    out.println "    @RequestMapping(method = RequestMethod.POST)"
    out.println "    WebResponse insert(@RequestBody Map<String,Object> map);"
    out.println ""
    out.println "    @RequestMapping(method = RequestMethod.PUT)"
    out.println "    WebResponse update(@RequestBody Map<String,Object> map);"
    out.println ""
    out.println "    @RequestMapping(value = \"{id}\", method = RequestMethod.DELETE)"
    out.println "    WebResponse delete(@PathVariable(\"id\") Long id);"
    out.println ""
    out.println "    @RequestMapping(value = \"/count\", method = RequestMethod.POST)"
    out.println "    WebResponse count(@RequestBody Map<String,Object> map);"
    out.println ""
    out.println "    @RequestMapping(value = \"/exist\", method = RequestMethod.POST)"
    out.println "    WebResponse exist(@RequestBody Map<String,Object> map);"
    out.println ""
    out.println "    @RequestMapping(value = \"/max\", method = RequestMethod.POST)"
    out.println "    WebResponse max(@RequestBody Map<String,Object> map);"
    out.println ""
    out.println "    @RequestMapping(value = \"/min\", method = RequestMethod.POST)"
    out.println "    WebResponse min(@RequestBody Map<String,Object> map);"
    out.println "}"

}


def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1? s : Case.LOWER.apply(s[0]) + s[1..-1]
}



def getPK(table)
{
    def fields = calcFields(table)
    index =0
    def pk
    fields.each {
        if(index ==0){
            pk = it.name.capitalize()
        }
        index++
    }
    return pk
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())

        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        def comm =[
                colName : col.getName(),
                name :  javaName(col.getName(), false),
                type : typeStr,
                commoent: col.getComment(),
                //annos: "\t@Column(name = \""+col.getName()+"\" )",
                annos: "",
        ]

        if("id".equals(Case.LOWER.apply(col.getName())))
            comm.annos +=["@Id"]
        fields += [comm]
    }
}
