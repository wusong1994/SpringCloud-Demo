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

typeMapping = [
        (~/(?i)bigint/)                          : "Long",
        (~/(?i)tinyint|int|smallint|mediumint/)  : "Integer",
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
    def className = javaName(tableName, true) + "Po"
    def fields = calcFields(table)

    packageName = getPackageName(dir)
    new File(dir, className + ".java").withPrintWriter("utf-8") { out -> generate(out, className, fields,table) }
}

// 获取包所在文件夹路径
def getPackageName(dir) {
    return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}

def generate(out, className, fields,table) {
    out.println "package $packageName"
    out.println ""
    out.println "import com.goumang.core.base.BasePo;"
    out.println ""
    out.println "import javax.persistence.GeneratedValue;"
    out.println "import javax.persistence.GenerationType;"
    out.println "import javax.persistence.Id;"
    out.println "import javax.persistence.Table;"

    Set types = new HashSet()

    fields.each() {
        types.add(it.type)
    }

    if (types.contains("Date")) {
        out.println "import java.util.Date;"
    }

    if (types.contains("InputStream")) {
        out.println "import java.io.InputStream;"
    }
    out.println ""
    out.println "@Table ( name =\""+table.getName() +"\" )"
    out.println "public class $className extends BasePo {"

    index = 0
    idName = "";
    fields.each() {
        out.println ""
        // 输出注释
        if (isNotEmpty(it.commoent)) {
            out.println "\t/* ${it.commoent} */"
        }

        if (it.annos != "") out.println "   ${it.annos}"
        //第一个字段有注解
        if (index == 0){
            index ++
            out.println"\t@Id"
            if(it.type=="Long" || it.type=="Integer"){
                out.println "\t@GeneratedValue(strategy = GenerationType.IDENTITY)"
            }
            idName = it.name
        }
        // 输出成员变量
        out.println "\tprivate ${it.type} ${it.name};"
    }

    // 输出get/set方法
    index = 0
    fields.each() {
        out.println ""
        if(index ==0){
            index ++
            out.println "\t@Override"
            out.println "\tpublic Object getPk() { return this.${it.name}; }"
            out.println ""
            out.println "\t@Override"
            if(it.type=="Long"){
                out.println "\tpublic void setPk(Object pk) { this.${it.name} = pk == null || pk.toString().length() == 0 ? null : Long.parseLong(pk.toString()); }"
            }else if(it.type=="String"){
                out.println "\tpublic void setPk(Object pk) { this.${it.name} = pk==null?null:pk.toString(); }"
            }else{
                out.println "\tpublic void setPk(Object pk) { this.${it.name} = (${it.type}) pk; }"
            }
            out.println ""
        }
        out.println "\tpublic ${it.type} get${it.name.capitalize()}() { return this.${it.name}; }"
        out.println ""
        out.println "\tpublic void set${it.name.capitalize()}(${it.type} ${it.name}) { this.${it.name} = ${it.name}; }"
    }
    out.println ""
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        System.out.println(spec)
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        def comm =[
                colName : col.getName(),
                name :  javaName(col.getName(), false),
                type : typeStr,
                commoent: col.getComment(),
                //annos: "\t@Column(name = \""+col.getName()+"\" )",
                annos:""
        ]

//    if("id".equals(Case.LOWER.apply(col.getName())))
//      comm.annos +=["@Id"]
        fields += [comm]
    }
}

// 处理类名（这里是因为我的表都是以t_命名的，所以需要处理去掉生成类名时的开头的T，
// 如果你不需要那么请查找用到了 javaClassName这个方法的地方修改为 javaName 即可）
def javaClassName(str, capitalize) {
    def s = str.split(/[^\p{Alnum}]/).collect { def s = Case.LOWER.apply(it).capitalize() }.join("")
    // 去除开头的T  http://developer.51cto.com/art/200906/129168.htm
    s = s[1..s.size() - 1]
    capitalize ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

def isNotEmpty(content) {
    return content != null && content.toString().trim().length() > 0
}

static String changeStyle(String str, boolean toCamel){
    if(!str || str.size() <= 1)
        return str

    if(toCamel){
        String r = str.toLowerCase().split('_').collect{cc -> Case.LOWER.apply(cc).capitalize()}.join('')
        return r[0].toLowerCase() + r[1..-1]
    }else{
        str = str[0].toLowerCase() + str[1..-1]
        return str.collect{cc -> ((char)cc).isUpperCase() ? '_' + cc.toLowerCase() : cc}.join('')
    }
}

static String genSerialID()
{
    return "\tprivate static final long serialVersionUID =  "+Math.abs(new Random().nextLong())+"L;";
}