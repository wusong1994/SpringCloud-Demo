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
        (~/(?i)tinyint|smallint|mediumint/)      : "number",
        (~/(?i)int/)                             : "number",
        (~/(?i)bool|bit/)                        : "bit",
        (~/(?i)float|double|decimal|real/)       : "number",
        (~/(?i)datetime|timestamp|date|time/)    : "datetime",
        (~/(?i)blob|binary|bfile|clob|raw|image/): "InputStream",
        (~/(?i)/)                                : "text"
]


FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
  SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}


def generate(table, dir) {
  def className = javaName(table.getName(), true)
  def fields = calcFields(table)

  packageName = getPackageName(dir)
  new File(dir, className + ".vue").withPrintWriter("utf-8") { out -> generate(out, className, fields,table) }
}

// 获取包所在文件夹路径
def getPackageName(dir) {
  return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}

def generate(out, className, fields,table) {
  out.println "<template>"
  out.println "    <div class=\"table\">"
  out.println "        <div class=\"container\">"
  out.println ""
  out.println "            <!-- \u641c\u7d22\u6846 -->"
  out.println "            <div class=\"handle-box\">"
  out.println "                <el-button type=\"primary\" icon=\"el-icon-plus\" @click=\"\$entity_add()\"></el-button>"
  out.println "                <el-input v-model=\"entity.table.params.name\" placeholder=\"\u7b5b\u9009\u5173\u952e\u8bcd\" style=\"width: 150px\" clearable></el-input>"
  out.println "                <el-button type=\"primary\" icon=\"el-icon-search\" @click=\"\$entity_page()\"></el-button>"
  out.println "            </div>"
  out.println ""
  out.println "            <!-- \u6570\u636e\u8868\u683c -->"
  out.println "            <el-table :data=\"entity.table.list\" ref=\"entity_table\" stripe size=\"mini\" class=\"table\" highlight-current-row>"
  out.println "                <el-table-column type=\"index\" width=\"55\" align=\"center\"></el-table-column>"
  index = 0;
  fields.each() {
    if(index!=0){
      if(it.type == "bit") {
        out.println "                <el-table-column prop=\"${it.name}\" label=\"${it.commoent}\">"
        out.println "                    <template slot-scope=\"scope\">"
        out.println "                        <el-switch v-model=\"scope.row.${it.name}\" active-color=\"#13ce66\" inactive-color=\"#ff4949\" @change=\"\$entity_switch(scope,'${it.name}')\">"
        out.println "                        </el-switch>"
        out.println "                    </template>"
        out.println "                </el-table-column>"
      }else{
        out.println "                <el-table-column prop=\"${it.name}\" label=\"${it.commoent}\"></el-table-column>"
      }
    }
    index ++
  }
  out.println "                <el-table-column label=\"\u64cd\u4f5c\" width=\"180\" align=\"center\">"
  out.println "                    <template slot-scope=\"scope\">"
  out.println "                        <el-button type=\"text\" icon=\"el-icon-edit\" @click=\"\$entity_edit(scope)\">\u7f16\u8f91</el-button>"
  out.println "                        <el-button type=\"text\" icon=\"el-icon-delete\" class=\"red\" @click=\"\$entity_del(scope)\">\u5220\u9664</el-button>"
  out.println "                    </template>"
  out.println "                </el-table-column>"
  out.println "            </el-table>"
  out.println ""
  out.println "            <!-- \u5206\u9875 -->"
  out.println "            <div class=\"pagination\">"
  out.println "                <el-pagination background layout=\"total,prev,pager,next\" :total=\"entity.table.recordsTotal\" :page-size=\"entity.table.pageSize\" :current-page=\"entity.table.pageNum\" @current-change=\"page\"></el-pagination>"
  out.println "            </div>"
  out.println "        </div>"
  out.println ""
  out.println "        <!-- \u7f16\u8f91\u5f39\u51fa\u6846 -->"
  out.println "        <el-dialog :title=\"entity.form.title\" :visible.sync=\"entity.form.visible\" @close=\"close\" width=\"48%\">"
  out.println "            <el-form :model=\"entity.form.data\" :rules=\"entity.form.rules\" ref=\"entity_form\" label-width=\"27%\" class=\"item-form\">"
  index = 0;
  fields.each() {
    if(index!=0){
      out.println "                <el-form-item label=\"${it.commoent}\" prop=\"${it.name}\">"
      if(it.type == "datetime"){
        out.println "                    <el-date-picker type=\"datetime\" v-model=\"entity.form.data.${it.name}\" value-format=\"yyyy-MM-dd HH:mm:ss\" style=\"width: 100%;\"></el-date-picker>"
      }else if(it.type == "bit"){
        out.println "                    <el-switch v-model=\"entity.form.data.${it.name}\" active-color=\"#13ce66\" inactive-color=\"#ff4949\"></el-switch>"
      } else{
        out.println "                    <el-input type=\"${it.type}\" v-model=\"entity.form.data.${it.name}\"></el-input>"
      }
      out.println "                </el-form-item>"
    }
    index ++
  }
  out.println "            </el-form>"
  out.println "            <span slot=\"footer\" class=\"dialog-footer\">"
  out.println "                <el-button @click=\"entity.form.visible = false\">\u53d6\u0020\u6d88</el-button>"
  out.println "                <el-button type=\"primary\" @click=\"\$entity_save()\">\u786e\u0020\u5b9a</el-button>"
  out.println "            </span>"
  out.println "        </el-dialog>"
  out.println ""
  out.println "    </div>"
  out.println "</template>"
  out.println ""
  out.println "<script>"
  out.println "    export default {"
  out.println "        name: \"$className\", //\u5c06\u9a7c\u5cf0\u8f6c\u6362\u4e3a'/'\u5e76\u4f5c\u4e3a\u8bbf\u95ee\u63a5\u53e3\u7684URL, \u4f8b\uff1aSysUser\u7684URL\u4e3a/console/sys/user"
  out.println "        data(){"
  out.println "            return {"
  out.println "                entity:{"
  out.println "                    table:{"
  out.println "                        index: -1, //\u884c\u7684\u4e0b\u6807"
  out.println "                        list: [],  //\u6570\u636e\u96c6\u5408"
  out.println "                        params: {}, //\u67e5\u8be2\u53c2\u6570"
  out.println "                        pageSize: 15, //\u6bcf\u9875\u5927\u5c0f"
  out.println "                        recordsTotal: 0 //\u603b\u8bb0\u5f55\u6570"
  out.println "                    },"
  out.println "                    form:{"
  out.print   "                        data:{"
  size = fields.size
  index = 0;
  fields.each() {
    out.print "${it.name}:null"
    if(index+1!=size){
      out.print ","
    }
    index ++;

  }
  out.print   "},\n"
  out.println "                        visible:false, //\u662f\u5426\u663e\u793a"
  out.println "                        title:'\u65b0\u589e', //\u6807\u9898"
  out.println "                        rules:{} //\u9a8c\u8bc1\u89c4\u5219"
  out.println "                    }"
  out.println "                }"
  out.println "            }"
  out.println "        },"
  out.println "        methods:{"
  out.println "            page(pageNum){"
  out.println "                this.\$entity_page(pageNum);"
  out.println "            },"
  out.println "            close(){"
  out.println "                this.\$entity_close();"
  out.println "            }"
  out.println "        },"
  out.println "        created(){"
  out.println "            this.\$entity_page();"
  out.println "        }"
  out.println "    }"
  out.println "</script>"
  out.println ""
  out.println "<style scoped>"
  out.println "    .red{"
  out.println "        color: #ff0000;"
  out.println "    }"
  out.println "</style>"
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
            annos: ""
    ]

    if("id".equals(Case.LOWER.apply(col.getName())))
      comm.annos +=["@Id"]
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