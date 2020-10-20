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
poPackageName = ""
servicePackageName = ""
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
  def serviceClassName = javaName(tableName, true) + "Service"
  def serviceName = javaName(tableName, false) + "Service"

  def poName = javaName(tableName, true) + "Po"
  def name = javaName(tableName, false)
  packageName = getPackageName(dir)
  def domain = packageName.toString().replace(".ctrl;","").split("\\.")
  def moduleName = domain[domain.length-1]
  if(name.toString().startsWith(moduleName)){
      name = name.toString().substring(moduleName.length())
      name = name.substring(0,1).toLowerCase() +name.substring(1);
  }

  name = domain[domain.length-1]+"/"+name
  poPackageName = getPoPackageName(packageName)
  servicePackageName = getServicePackageName(packageName)
  new File(dir, className + ".java").withPrintWriter("utf-8") { out -> generate(out, className, serviceClassName, serviceName, poName, name) }
}

// 获取包所在文件夹路径
def getPackageName(dir) {
  return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}
def getPoPackageName(packageName){
    return packageName.toString().replace(".ctrl;",".api.po.");
}
def getServicePackageName(packageName){
    return packageName.toString().replace(".ctrl;",".service.");
}

def generate(out, className, serviceClassName, serviceName, poName, name) {
  out.println "package $packageName"
  out.println ""
  out.println "import com.goumang.core.base.BaseCtrl;"
  out.println "import $poPackageName$poName"+";"
  out.println "import $servicePackageName$serviceClassName"+";"
  out.println "import org.springframework.beans.factory.annotation.Autowired;"
  out.println "import org.springframework.web.bind.annotation.RequestMapping;"
  out.println "import org.springframework.web.bind.annotation.RestController;"
  out.println ""
  out.println "@RestController"
  out.println "@RequestMapping(\"/$name\")"
  out.println "public class $className  extends BaseCtrl<$poName> {"
  out.println ""
  out.println "    @Autowired"
  out.println "    $serviceClassName $serviceName;"
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
