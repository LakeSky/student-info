package code;

import com.kzh.busi.model.Course;
import com.kzh.busi.model.Student;
import com.kzh.sys.model.Item;
import com.kzh.sys.service.generate.CodeTemplateUtil;
import com.kzh.sys.service.generate.CodeType;
import com.kzh.sys.service.generate.CodeUtils;
import org.junit.Test;

public class GenCode {
    @Test
    public void genFragments() {
        CodeUtils.generate(Item.class, CodeType.FRAGMENT);
    }

    //生成代码拷贝进去之后，记得重新生成一下工程Build->Rebuild Project
    @Test
    public void genFiles() {
        CodeUtils.generate(Student.class, CodeType.FILE);
    }

    @Test
    public void printResource() {
//        CodeTemplateUtil.generateQField(Role.class, true);
        CodeTemplateUtil.genQField(Course.class);
    }

    @Test
    public void printEnumDesc() {
        CodeUtils.getEnumDesc();
    }

    //生成数据库文档
    @Test
    public void genDatabaseDoc() throws Exception {
        CodeUtils.genDatabaseDoc();
    }

}
