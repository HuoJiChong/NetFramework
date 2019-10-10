package velly.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: admin
 * @Date: 2019/10/10
 * @Describe : 创建数据库脚本
 */
public class CreateDb {
    private String name;
    private List<String> sqlCreates;

    public CreateDb(Element ele) {
        name = ele.getAttribute("name");

        {
            sqlCreates = new ArrayList<>();
            NodeList sqls = ele.getElementsByTagName("sql_createTable");
            for (int i = 0;i<sqls.getLength();i++){
                String sqlCreate = sqls.item(i).getTextContent();
                this.sqlCreates.add(sqlCreate);
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getSqlCreates() {
        return sqlCreates;
    }
}
