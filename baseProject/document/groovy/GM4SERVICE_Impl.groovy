import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = ""
poPackageName = ""
servicePackageName = ""

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
  SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}


def generate(table, dir) {
  def tableName = table.getName();
  if(tableName.matches("t_.+")){
    tableName = tableName.substring(2)
  }
  def className = javaName(tableName, true) + "ServiceImpl"
  def serviceName = javaName(tableName, true) + "Service"
  def poName = javaName(tableName, true) + "Po"
  packageName = getPackageName(dir)
  poPackageName = getPoPackageName(packageName)
  servicePackageName = getServicePackageName(packageName)
  new File(dir, className + ".java").withPrintWriter("utf-8") { out -> generate(out, className, serviceName, poName) }
}

// 获取包所在文件夹路径
def getPackageName(dir) {
  return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}
def getPoPackageName(packageName){
  return packageName.toString().replace(".service.impl;",".api.po.");
}
def getServicePackageName(packageName){
  return packageName.toString().replace(".service.impl;",".service.");
}
def generate(out, className, serviceName, poName) {
  out.println "package $packageName"
  out.println ""
  out.println "import com.goumang.core.base.BaseServiceImpl;"
  out.println "import com.goumang.core.util.MapperUtil;"
  out.println "import org.springframework.stereotype.Service;"
  out.println "import tk.mybatis.mapper.weekend.Weekend;"
  out.println "import tk.mybatis.mapper.weekend.WeekendCriteria;"
  out.println "import $poPackageName$poName"+";"
  out.println "import $servicePackageName$serviceName"+";"
  out.println ""
  out.println "import java.util.List;"
  out.println ""
  out.println "@Service"
  out.println "public class $className extends BaseServiceImpl<$poName> implements $serviceName {"
  out.println ""
  out.println "    @Override"
  out.println "    public List<$poName> selectForList($poName po){"
  out.println "        Weekend wk = Weekend.of($poName"+".class);"
  out.println "        WeekendCriteria<$poName,Object> criteria = wk.weekendCriteria();"
  out.println ""
  out.println "        MapperUtil.setAndEqual(po,criteria,true);"
  out.println "        MapperUtil.setOrderBy(po,wk);"
  out.println ""
  out.println "        List<$poName> list = select(wk);"
  out.println "        return list;"
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




