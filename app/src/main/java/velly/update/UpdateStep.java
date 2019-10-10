package velly.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: admin
 * @Date: 2019/10/10
 * @Describe :
 */
public class UpdateStep {
    /**
     * 旧版本
     */
    private String versionFrom;

    /**
     * 新版本
     */
    private String versionTo;

    /**
     * 更新数据库脚本
     */
    private List<UpdateDb> updateDbs;

    // ==================================================

    public UpdateStep(Element ele)
    {
        versionFrom = ele.getAttribute("versionFrom");
        versionTo = ele.getAttribute("versionTo");
        updateDbs = new ArrayList<UpdateDb>();

        NodeList dbs = ele.getElementsByTagName("updateDb");
        for (int i = 0; i < dbs.getLength(); i++)
        {
            Element db = (Element) (dbs.item(i));
            UpdateDb updateDb = new UpdateDb(db);
            this.updateDbs.add(updateDb);
        }
    }

    public String getVersionFrom() {
        return versionFrom;
    }

    public String getVersionTo() {
        return versionTo;
    }

    public List<UpdateDb> getUpdateDbs() {
        return updateDbs;
    }
}
