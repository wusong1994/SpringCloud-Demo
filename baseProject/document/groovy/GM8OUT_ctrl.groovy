import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil
/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = ""
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
  def className = javaName(tableName, true) + "Ctrl"
  def feignClassName = javaName(tableName, true) + "Feign"
  def feignName = javaName(tableName,false)+"Feign"

  packageName = getPackageName(dir)
  def path = getPath(tableName)
  def feignPackage = getFeignPackage(packageName);

  new File(dir, className + ".java").withPrintWriter("utf-8") { out -> generate(out, className, feignClassName, feignName, feignPackage, path) }
}

// 获取包所在文件夹路径
def getPackageName(dir) {
  return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}
def getPath(tableName){
  def path = javaName(tableName, false)
  def domain = packageName.toString().replace(";","")split("\\.")
  def moduleName = domain[domain.length-1]
  if(path.toString().startsWith(moduleName)){
    path = path.toString().substring(moduleName.length())
    path = path.substring(0,1).toLowerCase() +path.substring(1);
  }

  path = domain[domain.length-1]+"/"+path
  return path
}
def getFeignPackage(packageName){
  def feignPackage = packageName
  feignPackage = feignPackage.toString().replaceAll("\\.[a-zA-Z]+\\.ctrl","").replace("package ","").replace(";",".api.feign")

  return feignPackage
}

def generate(out, className, feignClassName, feignName, feignPackage, path) {
  out.println "package $packageName"
  out.println ""
  out.println "import com.goumang.core.web.WebResponse;"
  out.println "import $feignPackage.$feignClassName;"
  out.println "import org.springframework.beans.factory.annotation.Autowired;"
  out.println "import org.springframework.web.bind.annotation.*;"
  out.println ""
  out.println "import java.util.Map;"
  out.println ""
  out.println "@RestController"
  out.println "@RequestMapping(\"/console/$path\")"
  out.println "public class $className {"
  out.println ""
  out.println "    @Autowired"
  out.println "    private $feignClassName $feignName;"
  out.println ""
  out.println "    @RequestMapping(value = \"{id}\", method = RequestMethod.GET)"
  out.println "    public WebResponse info(@PathVariable(\"id\") Long id){"
  out.println "        return $feignName"+".info(id);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(value = \"/info\", method = RequestMethod.POST)"
  out.println "    public WebResponse info(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".info(map);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(value = \"/list\", method = RequestMethod.POST)"
  out.println "    public WebResponse list(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".list(map);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(value = \"/page\", method = RequestMethod.POST)"
  out.println "    public WebResponse page(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".page(map);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(method = RequestMethod.POST)"
  out.println "    public WebResponse insert(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".insert(map);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(method = RequestMethod.PUT)"
  out.println "    public WebResponse update(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".update(map);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(value = \"{id}\", method = RequestMethod.DELETE)"
  out.println "    public WebResponse delete(@PathVariable(\"id\") Long id){"
  out.println "        return $feignName"+".delete(id);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(value = \"/count\", method = RequestMethod.POST)"
  out.println "    public WebResponse count(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".count(map);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(value = \"/exist\", method = RequestMethod.POST)"
  out.println "    public WebResponse exist(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".exist(map);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(value = \"/max\", method = RequestMethod.POST)"
  out.println "    public WebResponse max(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".max(map);"
  out.println "    }"
  out.println ""
  out.println "    @RequestMapping(value = \"/min\", method = RequestMethod.POST)"
  out.println "    public WebResponse min(@RequestBody Map<String,Object> map){"
  out.println "        return $feignName"+".min(map);"
  out.println "    }"
  out.println ""
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
