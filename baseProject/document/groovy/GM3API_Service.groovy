import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.sql.Date


/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = ""

poPackageName = ""

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
  SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}


def generate(table, dir) {
  def tableName = table.getName();
  if(tableName.matches("t_.+")){
    tableName = tableName.substring(2)
  }
  def className = javaName(tableName, true) + "Service"
  def poName = javaName(tableName, true) + "Po"
  packageName = getPackageName(dir)
  poPackageName = getPoPackageName(packageName)
  new File(dir, className + ".java").withPrintWriter("utf-8") { out -> generate(out, className, poName) }
}

// 获取包所在文件夹路径
def getPackageName(dir) {
  return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}

def getPoPackageName(packageName){
  return packageName.toString().replace(".service;",".api.po.");
}
def generate(out, className, poName) {
  out.println "package $packageName"
  out.println ""
  out.println "import com.goumang.core.base.BaseService;"
  out.println "import $poPackageName$poName"+";"
  out.println ""
  out.println "public interface $className extends BaseService<$poName> {"
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




