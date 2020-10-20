SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `status` varchar(10) DEFAULT NULL COMMENT '状态，1正常，0无效',
  `userName` varchar(20) DEFAULT NULL COMMENT '用户名称',
  `loginName` varchar(20) DEFAULT NULL COMMENT '登录名',
  `phone` varchar(15) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `userImage` varchar(127) DEFAULT NULL COMMENT '头像',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `operDate` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`userId`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '1', 'admin', 'admin', '', '', 'e10adc3949ba59abbe56e057f20f883e', null, '2019-08-14 11:06:31', '2019-10-11 18:00:03');

-- ----------------------------
-- Table structure for sys_dic_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dic_type`;
CREATE TABLE `sys_dic_type` (
  `dityId` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典分类ID',
  `code` varchar(25) DEFAULT NULL COMMENT '编码',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`dityId`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='字典分类表';

-- ----------------------------
-- Records of sys_dic_type
-- ----------------------------
INSERT INTO `sys_dic_type` VALUES ('1', 'USER_STATUS', '用户状态');

-- ----------------------------
-- Table structure for sys_dic
-- ----------------------------
DROP TABLE IF EXISTS `sys_dic`;
CREATE TABLE `sys_dic` (
  `dicId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` varchar(20) DEFAULT NULL COMMENT '类型编码',
  `code` varchar(20) DEFAULT NULL COMMENT '编码',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`dicId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Records of sys_dic
-- ----------------------------
INSERT INTO `sys_dic` VALUES ('1', 'USER_STATUS', '1', '正常', '1');
INSERT INTO `sys_dic` VALUES ('2', 'USER_STATUS', '0', '禁用', '2');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `roleId` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `roleCode` varchar(15) DEFAULT NULL,
  `roleName` varchar(20) DEFAULT NULL COMMENT '名称',
  `roleRemark` varchar(127) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`roleId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for sys_role_action
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_action`;
CREATE TABLE `sys_role_action` (
  `roacId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `roleId` int(11) DEFAULT NULL COMMENT '角色ID',
  `funcId` int(11) DEFAULT NULL COMMENT '功能点ID',
  `actionId` int(11) DEFAULT NULL COMMENT '操作点ID',
  PRIMARY KEY (`roacId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色操作表';

-- ----------------------------
-- Table structure for sys_role_func
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_func`;
CREATE TABLE `sys_role_func` (
  `rofuId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `roleId` int(11) DEFAULT NULL COMMENT '角色ID',
  `funcId` int(11) DEFAULT NULL COMMENT '功能点ID',
  PRIMARY KEY (`rofuId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色功能表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `usroId` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` int(11) DEFAULT NULL COMMENT '用户ID',
  `roleId` int(11) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`usroId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Table structure for sys_action
-- ----------------------------
DROP TABLE IF EXISTS `sys_action`;
CREATE TABLE `sys_action` (
  `actionId` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作点ID',
  `funcId` int(11) DEFAULT NULL COMMENT '功能点ID',
  `method` varchar(10) DEFAULT NULL,
  `actionCode` varchar(50) DEFAULT NULL COMMENT '代码，唯一',
  `actionName` varchar(20) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`actionId`)
) ENGINE=MyISAM AUTO_INCREMENT=108 DEFAULT CHARSET=utf8 COMMENT='操作点表';

-- ----------------------------
-- Records of sys_action
-- ----------------------------
INSERT INTO `sys_action` VALUES ('24', '5', 'PUT', '*', '更新');
INSERT INTO `sys_action` VALUES ('25', '5', 'DELETE', '{id}', '删除');
INSERT INTO `sys_action` VALUES ('26', '5', 'POST', 'list', '查询-列表');
INSERT INTO `sys_action` VALUES ('27', '5', 'POST', '*', '新增');
INSERT INTO `sys_action` VALUES ('28', '5', 'GET', '{id}', '查询-单个');
INSERT INTO `sys_action` VALUES ('29', '5', 'POST', 'page', '查询-分页');
INSERT INTO `sys_action` VALUES ('30', '5', 'POST', 'exist', '查询-是否存在');
INSERT INTO `sys_action` VALUES ('31', '6', 'PUT', '*', '更新');
INSERT INTO `sys_action` VALUES ('32', '6', 'DELETE', '{id}', '删除');
INSERT INTO `sys_action` VALUES ('33', '6', 'POST', 'list', '查询-列表');
INSERT INTO `sys_action` VALUES ('34', '6', 'POST', '*', '新增');
INSERT INTO `sys_action` VALUES ('35', '6', 'GET', '{id}', '查询-单个');
INSERT INTO `sys_action` VALUES ('36', '6', 'POST', 'page', '查询-分页');
INSERT INTO `sys_action` VALUES ('37', '6', 'POST', 'exist', '查询-是否存在');
INSERT INTO `sys_action` VALUES ('38', '6', 'POST', 'pageJoinRole', '查询-角色拥有的操作点');
INSERT INTO `sys_action` VALUES ('39', '6', 'POST', 'addDefault/{funcId}', '新增-默认操作');
INSERT INTO `sys_action` VALUES ('51', '9', 'DELETE', '*', '删除');
INSERT INTO `sys_action` VALUES ('52', '9', 'POST', 'batchOperation', '更新-授权');
INSERT INTO `sys_action` VALUES ('53', '10', 'PUT', '*', '更新');
INSERT INTO `sys_action` VALUES ('54', '10', 'DELETE', '{id}', '删除');
INSERT INTO `sys_action` VALUES ('55', '10', 'POST', 'list', '查询-列表');
INSERT INTO `sys_action` VALUES ('56', '10', 'POST', '*', '新增');
INSERT INTO `sys_action` VALUES ('57', '10', 'POST', 'sort', '更新-排序');
INSERT INTO `sys_action` VALUES ('58', '10', 'GET', '{id}', '查询-单个');
INSERT INTO `sys_action` VALUES ('59', '10', 'POST', 'page', '查询-分页');
INSERT INTO `sys_action` VALUES ('60', '10', 'POST', 'exist', '查询-是否存在');
INSERT INTO `sys_action` VALUES ('61', '11', 'PUT', 'disable/{userId}', '更新-禁用');
INSERT INTO `sys_action` VALUES ('62', '11', 'PUT', 'enable/{userId}', '更新-解禁');
INSERT INTO `sys_action` VALUES ('63', '11', 'PUT', '*', '更新');
INSERT INTO `sys_action` VALUES ('64', '11', 'POST', '*', '新增');
INSERT INTO `sys_action` VALUES ('65', '11', 'POST', 'page', '查询-分页');
INSERT INTO `sys_action` VALUES ('66', '11', 'PUT', 'resetPwd', '更新-密码');
INSERT INTO `sys_action` VALUES ('67', '11', 'POST', 'joinPage', '查询-分页');
INSERT INTO `sys_action` VALUES ('68', '12', 'POST', 'batchOperation', '更新-授权');
INSERT INTO `sys_action` VALUES ('69', '13', 'POST', 'batchOperation', '更新-授权');
INSERT INTO `sys_action` VALUES ('70', '14', 'PUT', '*', '更新');
INSERT INTO `sys_action` VALUES ('71', '14', 'DELETE', '{id}', '删除');
INSERT INTO `sys_action` VALUES ('72', '14', 'POST', 'list', '查询-列表');
INSERT INTO `sys_action` VALUES ('73', '14', 'POST', '*', '新增');
INSERT INTO `sys_action` VALUES ('74', '14', 'GET', '{id}', '查询-单个');
INSERT INTO `sys_action` VALUES ('75', '14', 'POST', 'info', '查询-单个');
INSERT INTO `sys_action` VALUES ('76', '14', 'POST', 'page', '查询-分页');
INSERT INTO `sys_action` VALUES ('77', '14', 'POST', 'exist', '查询-是否存在');
INSERT INTO `sys_action` VALUES ('85', '16', 'PUT', '*', '更新');
INSERT INTO `sys_action` VALUES ('86', '16', 'DELETE', '{id}', '删除');
INSERT INTO `sys_action` VALUES ('87', '16', 'POST', 'list', '查询-列表');
INSERT INTO `sys_action` VALUES ('88', '16', 'POST', '*', '新增');
INSERT INTO `sys_action` VALUES ('89', '16', 'GET', '{id}', '查询-单个');
INSERT INTO `sys_action` VALUES ('90', '16', 'POST', 'page', '查询-分页');
INSERT INTO `sys_action` VALUES ('91', '16', 'POST', 'exist', '查询-是否存在');
INSERT INTO `sys_action` VALUES ('92', '16', 'POST', 'pageJoinRole', '查询-角色功能点');
INSERT INTO `sys_action` VALUES ('93', '16', 'POST', 'saveAllApi', '新增-API');
INSERT INTO `sys_action` VALUES ('94', '17', 'PUT', '*', '更新');
INSERT INTO `sys_action` VALUES ('95', '17', 'DELETE', '{id}', '删除');
INSERT INTO `sys_action` VALUES ('96', '17', 'POST', '*', '新增');
INSERT INTO `sys_action` VALUES ('97', '17', 'GET', '{id}', '查询-单个');
INSERT INTO `sys_action` VALUES ('98', '17', 'POST', 'page', '查询-分页');
INSERT INTO `sys_action` VALUES ('99', '17', 'POST', 'exist', '查询-是否存在');
INSERT INTO `sys_action` VALUES ('100', '18', 'PUT', '*', '更新');
INSERT INTO `sys_action` VALUES ('101', '18', 'DELETE', '{id}', '删除-单个');
INSERT INTO `sys_action` VALUES ('102', '18', 'POST', '*', '新增');
INSERT INTO `sys_action` VALUES ('103', '18', 'POST', 'sort', '更新-排序');
INSERT INTO `sys_action` VALUES ('104', '18', 'POST', 'tree', '查询-树');
INSERT INTO `sys_action` VALUES ('105', '18', 'GET', '{id}', '查询-单个');
INSERT INTO `sys_action` VALUES ('106', '18', 'POST', 'page', '查询-分页');
INSERT INTO `sys_action` VALUES ('107', '18', 'DELETE', 'deleteTree/{menuId}', '删除-节点');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menuId` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parentId` int(11) DEFAULT NULL COMMENT '父Id',
  `funcIds` varchar(127) DEFAULT NULL COMMENT '功能点ID，多个逗号隔开',
  `menuTitle` varchar(10) DEFAULT NULL COMMENT '标题',
  `menuPath` varchar(50) DEFAULT NULL COMMENT '路径',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT NULL COMMENT '序号',
  `home` bit(1) DEFAULT b'0' COMMENT '是否主页',
  PRIMARY KEY (`menuId`)
) ENGINE=MyISAM AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '11', '用户管理', 'sys/SysUser', 'fa-user-o', '1', true);
INSERT INTO `sys_menu` VALUES ('2', '3', '16', '功能点', 'sys/SysFunc', null, '1', false);
INSERT INTO `sys_menu` VALUES ('3', '0', '16,17', '权限管理', null, 'fa-shield', '3', false);
INSERT INTO `sys_menu` VALUES ('4', '3', '17', '角色', 'sys/SysRole', null, '2', false);
INSERT INTO `sys_menu` VALUES ('7', '0', '14', '系统属性', 'sys/SysProperties', 'fa-cogs', '4', false);
INSERT INTO `sys_menu` VALUES ('5', '0', '10', '字典管理', 'sys/SysDicType', 'fa-book', '2', false);
INSERT INTO `sys_menu` VALUES ('6', '0', '18', '菜单管理', 'sys/SysMenu', 'fa-bars', '6', false);
INSERT INTO `sys_menu` VALUES ('8', '0', NULL, '文件管理', 'sys/Oss', 'fa-hdd-o', '5', false);

-- ----------------------------
-- Table structure for sys_func
-- ----------------------------
DROP TABLE IF EXISTS `sys_func`;
CREATE TABLE `sys_func` (
  `funcId` int(11) NOT NULL AUTO_INCREMENT COMMENT '功能点ID',
  `funcCode` varchar(50) DEFAULT NULL COMMENT '代码，唯一',
  `funcName` varchar(20) DEFAULT NULL COMMENT '名称',
  `funcValue` varchar(127) DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`funcId`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='功能点表';

-- ----------------------------
-- Records of sys_func
-- ----------------------------
INSERT INTO `sys_func` VALUES ('5', 'sys/dicType', '系统-字典类型', null);
INSERT INTO `sys_func` VALUES ('6', 'sys/action', '系统-操作点', null);
INSERT INTO `sys_func` VALUES ('9', 'sys/roleAction', '系统-角色操作点', null);
INSERT INTO `sys_func` VALUES ('10', 'sys/dic', '系统-字典', null);
INSERT INTO `sys_func` VALUES ('11', 'sys/user', '系统-用户', null);
INSERT INTO `sys_func` VALUES ('12', 'sys/userRole', '系统-用户角色', null);
INSERT INTO `sys_func` VALUES ('13', 'sys/roleFunc', '系统-角色功能点', null);
INSERT INTO `sys_func` VALUES ('14', 'sys/properties', '系统-属性', null);
INSERT INTO `sys_func` VALUES ('16', 'sys/func', '系统-功能点', null);
INSERT INTO `sys_func` VALUES ('17', 'sys/role', '系统-角色', null);
INSERT INTO `sys_func` VALUES ('18', 'sys/menu', '系统-菜单', null);

-- ----------------------------
-- Table structure for sys_properties
-- ----------------------------
DROP TABLE IF EXISTS `sys_properties`;
CREATE TABLE `sys_properties` (
  `propId` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `propKey` varchar(60) DEFAULT NULL COMMENT '键',
  `propName` varchar(30) DEFAULT NULL COMMENT '名称',
  `propValue` varchar(120) DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`propId`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统属性表';