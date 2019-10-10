package velly.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: admin
 * @Date: 2019/10/10
 * @Describe : 数据库升级创建表脚本
 */
public class CreateVersion {
    private String version;

    private List<CreateDb> createDbs;

    public void setVersion(String version) {
        this.version = version;
    }

    public CreateVersion(Element ele) {
        version = ele.getAttribute("version");{
            createDbs = new ArrayList<>();
            NodeList cs = ele.getElementsByTagName("createDb");
            for (int i = 0;i<cs.getLength();i++){
                Element ci = (Element) cs.item(i);
                CreateDb cd = new CreateDb(ci);
                this.createDbs.add(cd);
            }
        }
    }

    public String getVersion() {
        return version;
    }

    public List<CreateDb> getCreateDbs() {
        return createDbs;
    }

}
