代码仅供参考，完整代码请联系作者

#学生信息管理系统

### 包含
1. 年级信息管理增删改查及导出
2. 课程信息管理增删改查及导出
3. 班级信息管理增删改查及导出
4. 教师信息管理增删改查及导出
5. 学生信息管理增删改查及导出
6. 班级可以选择课程和老师
7. 老师也可以选择所教的班级和课程
8. 代码简洁易读


### 1.软件安装
1. 新建数据库
2. 导入studentinfo-init.sql
3. 配置jdbc.properties
4. 启动tomcat

### 2.项目特点**
1. 纯Spring MVC技术栈，无DTO，没有鸡肋的interface,impl。
2. 扩展SpringSecurity权限控制，精细到任意请求的权限控制。
3. 资源无需手动录入数据库，根据注解自动生成菜单和权限列表，只需选择即可。


**演示效果图：**
![输入图片说明](http://open.mutou888.com/studentinfo/grade.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/clazz.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/class-add.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/class.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/teacher.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/teacher-add.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/student.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/student-add.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/menu.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/user.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/role.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/studentinfo/code.png "在这里输入图片标题")

**部分代码：**
 ```
    @QClass(name = "年级")
    @Entity
    @Table(name = "b_grade")
    public class Grade extends BaseEntity {
        @QField(name = "名称", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like, nullable = false)
        private String name; //名称
    
        @OneToMany(mappedBy = "grade")
        @JsonBackReference
        private Set<Clazz> clazzes; //班级
    
        @OneToMany(mappedBy = "grade")
        @JsonBackReference
        private Set<Course> courses; //课程
    }
    
    @QClass(name = "课程")
    @Entity
    @Table(name = "b_course")
    public class Course extends BaseEntity {
        @QField(name = "名称", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
        private String name; //名称
    
        @ManyToOne
        @JoinColumn(name = "grade_id")
        @JsonManagedReference
        private Grade grade; //所属年级
    
        @ManyToMany(cascade = {CascadeType.REMOVE}, mappedBy = "courses")
        @JsonBackReference
        private Set<Clazz> clazzes;
    
        @ManyToMany(cascade = {CascadeType.REMOVE})
        @JoinTable(name = "b_course_teacher", joinColumns = {@JoinColumn(name = "courses_id")}, inverseJoinColumns = {@JoinColumn(name = "teachers_id")})
        @JsonManagedReference
        private Set<Teacher> teachers;
    
        @ManyToMany(cascade = {CascadeType.REMOVE})
        @JoinTable(name = "b_course_student")
        @JsonManagedReference
        private Set<Student> students; 
    }
 ```

联系方式：QQ 2644328654(月牙儿)，支持定制开发


基于javacat极简开发框架
http://www.mutou888.com/javacat/index.html

现在的快速开发脚手架铺天盖地，功能越来越复杂，上手难度越来越高，基本上都是功能的堆积，代码臃肿，真正能在开发效率上有所改进的少之又少，
使用的人茫然无措，只是当做一个黑盒子来用，代码的可读性，可控性都很差，
但是很多人其实需要这样的开发框架，代码简洁，但是基本的菜单管理，角色管理，用户管理，权限控制都有，代码清晰，自己写的代码在自己的控制范围之内，
简而精致，是我们不懈的追求，当别人堆积功能堆积代码的时候，我们逆流而上，重构精简现有的功能，从根本上提高开发的效率和代码的可读性，可控性。

系统亮点：

精细的菜单角色权限控制管理。

1.角色精细配置：所有的角色由初始的系统管理员创建，每个后创建的角色，只能选择自己角色内的菜单和资源进行分配。

比如一个公司管理员，他具有公司管理的菜单和资源，那么他在新建其他角色的时候只能从自己所有的菜单和资源中组合产生新的角色。

2.数据权限控制：部分管理员只能看到部门的数据，个人只能看到个人的数据，再分配角色的数据权限只能在当前角色可分配的范围之内

如：店铺管理员的再分配角色的数据权限，最大的可见数据范围应该是本店铺，不能超出。

3.权限可配置：角色能够对最小粒度的资源单位进行有效的管理，比如按钮或者请求或者单纯的逻辑请求进行管理，精确配置每个角色能够看到的按钮。

一般系统的做法是手动添加每个需要控制的url到数据库，然后配置到数据库，这样不仅容易出错，而且费时费力。
改进：直接通过扫描Controller的方式获取所有需要控制的资源，这样就避免了手动添加资源到数据库的烦恼。

