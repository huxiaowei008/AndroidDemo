package com.hxw.frame.update;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hxw on 2016/11/10.
 */

public class UpdateItem {

    /**
     * objectJsonName : appslc_1478755176458
     * objectJson : [{"tjbb":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"int","sdefaultvalue":"","sfield":"tjbb","sfieldref":"sname","srefkey":"","stitle":"推荐版本","suitype":"bool","suitypeexp":""},"iddep":{"idjoinobject":"dep","idobject":"appslc","lcaninput":"1","lcannotrepeat":"","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"1","sdbtype":"string","sdefaultvalue":"$sys(dep)","sfield":"iddep","sfieldref":"sname","srefkey":"","stitle":"负责部门","suitype":"ref","suitypeexp":"normal"},"updatedby":{"idjoinobject":"employee","idobject":"appslc","lcaninput":"","lcannotrepeat":"","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"","sdbtype":"string","sdefaultvalue":"$sys(employee)","sfield":"updatedby","sfieldref":"sname","srefkey":"","stitle":"编辑用户","suitype":"ref","suitypeexp":"normal"},"gxnr":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"string","sdefaultvalue":"","sfield":"gxnr","sfieldref":"sname","srefkey":"","stitle":"更新内容","suitype":"string","suitypeexp":"big"},"created":{"idjoinobject":"","idobject":"appslc","lcaninput":"","lcannotrepeat":"","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"","sdbtype":"datetime","sdefaultvalue":"$sys(now)","sfield":"created","sfieldref":"","srefkey":"","stitle":"新建时间","suitype":"datetime","suitypeexp":"long"},"rjbbh":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"string","sdefaultvalue":"","sfield":"rjbbh","sfieldref":"sname","srefkey":"","stitle":"软件版本号","suitype":"string","suitypeexp":"normal"},"cjr":{"idjoinobject":"employee","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"string","sdefaultvalue":"","sfield":"cjr","sfieldref":"sname","srefkey":"13002637001710527179865071935319","stitle":"创建人","suitype":"ref","suitypeexp":"normal"},"xzcs":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"int","sdefaultvalue":"","sfield":"xzcs","sfieldref":"sname","srefkey":"","stitle":"下载次数","suitype":"int","suitypeexp":""},"rjbb":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"string","sdefaultvalue":"","sfield":"rjbb","sfieldref":"sname","srefkey":"","stitle":"软件版本","suitype":"string","suitypeexp":"normal"},"iosewm":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"string","sdefaultvalue":"","sfield":"iosewm","sfieldref":"sname","srefkey":"","stitle":"IOS二维码","suitype":"picture","suitypeexp":""},"cjsj":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"datetime","sdefaultvalue":"","sfield":"cjsj","sfieldref":"sname","srefkey":"","stitle":"创建时间","suitype":"datetime","suitypeexp":"long"},"rjgs":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"string","sdefaultvalue":"","sfield":"rjgs","sfieldref":"sname","srefkey":"c8460f8f10320b531c98c999a4624d4f","stitle":"软件格式","suitype":"enum","suitypeexp":"normal"},"createdby":{"idjoinobject":"employee","idobject":"appslc","lcaninput":"","lcannotrepeat":"","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"","sdbtype":"string","sdefaultvalue":"$sys(employee)","sfield":"createdby","sfieldref":"sname","srefkey":"","stitle":"新建用户","suitype":"ref","suitypeexp":"normal"},"sname":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"1","sdbtype":"string","sdefaultvalue":"","sfield":"sname","sfieldref":"","srefkey":"","stitle":"名称","suitype":"string","suitypeexp":"normal"},"czrjid":{"idjoinobject":"appslt","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"string","sdefaultvalue":"","sfield":"czrjid","sfieldref":"rjmz","srefkey":"","stitle":"参照软件ID","suitype":"ref","suitypeexp":"normal"},"tjrj":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"int","sdefaultvalue":"","sfield":"tjrj","sfieldref":"sname","srefkey":"","stitle":"推荐软件","suitype":"bool","suitypeexp":""},"updated":{"idjoinobject":"","idobject":"appslc","lcaninput":"","lcannotrepeat":"","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"","sdbtype":"datetime","sdefaultvalue":"$sys(now)","sfield":"updated","sfieldref":"","srefkey":"","stitle":"编辑时间","suitype":"datetime","suitypeexp":"long"},"idowner":{"idjoinobject":"employee","idobject":"appslc","lcaninput":"1","lcannotrepeat":"","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"1","sdbtype":"string","sdefaultvalue":"$sys(employee)","sfield":"idowner","sfieldref":"sname","srefkey":"","stitle":"负责人","suitype":"ref","suitypeexp":"normal"},"appwj":{"idjoinobject":"","idobject":"appslc","lcaninput":"1","lcannotrepeat":"0","lcanorder":"1","lcanoutput":"1","lcansearch":"1","lmustinput":"0","sdbtype":"string","sdefaultvalue":"","sfield":"appwj","sfieldref":"sname","srefkey":"","stitle":"app文件","suitype":"file","suitypeexp":""}}]
     * query : [{"id":"2700dad0ad61231f26ed490c6f6879a9","lpub":"1","stitle":"显示列表"},{"id":"9a6410fb7f16d983504d7046dcdc63be","lpub":"1","stitle":"跟随显示"}]
     * cordition : [{"sfield":"czrjid","scomparison":"eqref","svalue":"287d4256a34742d6497f0b68f4824b49"}]
     * isNED : {"new":"0","edit":"0","del":"0"}
     * curPageNo : 1
     * idquery : 2700dad0ad61231f26ed490c6f6879a9
     * count : 15
     * code : 600
     * commonQueryObject : [[{"key":"rowid","value":1,"valuename":null},{"key":"id","value":"37d87ae3e14fbc120eefab4d586b2c0b","valuename":null},{"key":"czrjid","value":"287d4256a34742d6497f0b68f4824b49","valuename":null},{"key":"czrjidname","value":"企业点对点(android)","valuename":null},{"key":"rjgs","value":"751b8a50f044dd071b73289014433f9e","valuename":null},{"key":"rjgsname","value":"apk","valuename":null},{"key":"rjbbh","value":"24","valuename":null},{"key":"gxnr","value":"1、修改部分bug\n2、部分界面样式修改\n3、问题查看附件修改等","valuename":null},{"key":"cjsj","value":"2016-11-05T17:04:00","valuename":null},{"key":"cjr","value":"091a80ff62c8b4c6f1a3e2f51b9b6f37","valuename":null},{"key":"cjrname","value":"胡晓伟","valuename":null},{"key":"appwj","value":"9ff6fb8d145b1787c75df734ece71be7.apk;","valuename":null},{"key":"xzcs","value":null,"valuename":null},{"key":"tjbb","value":-1,"valuename":null},{"key":"tjrj","value":-1,"valuename":null},{"key":"iosewm","value":null,"valuename":null},{"key":"appwj_title","value":"V1.24_20161105_hxw_dev.apk;","valuename":null},{"key":"iosewm_title","value":null,"valuename":null}]]
     * queryshow : id,czrjid,rjgs,rjbbh,gxnr,cjsj,cjr,appwj,xzcs,tjbb,tjrj,iosewm
     */

    private String objectJsonName;
    private String objectJson;
    /**
     * new : 0
     * edit : 0
     * del : 0
     */

    private IsNEDBean isNED;
    private int curPageNo;
    private String idquery;
    private int count;
    private String code;
    private String queryshow;
    /**
     * id : 2700dad0ad61231f26ed490c6f6879a9
     * lpub : 1
     * stitle : 显示列表
     */

    private List<QueryBean> query;
    /**
     * sfield : czrjid
     * scomparison : eqref
     * svalue : 287d4256a34742d6497f0b68f4824b49
     */

    private List<CorditionBean> cordition;
    /**
     * key : rowid
     * value : 1
     * valuename : null
     */

    private List<List<CommonQueryObjectBean>> commonQueryObject;

    public String getObjectJsonName() {
        return objectJsonName;
    }

    public void setObjectJsonName(String objectJsonName) {
        this.objectJsonName = objectJsonName;
    }

    public String getObjectJson() {
        return objectJson;
    }

    public void setObjectJson(String objectJson) {
        this.objectJson = objectJson;
    }

    public IsNEDBean getIsNED() {
        return isNED;
    }

    public void setIsNED(IsNEDBean isNED) {
        this.isNED = isNED;
    }

    public int getCurPageNo() {
        return curPageNo;
    }

    public void setCurPageNo(int curPageNo) {
        this.curPageNo = curPageNo;
    }

    public String getIdquery() {
        return idquery;
    }

    public void setIdquery(String idquery) {
        this.idquery = idquery;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQueryshow() {
        return queryshow;
    }

    public void setQueryshow(String queryshow) {
        this.queryshow = queryshow;
    }

    public List<QueryBean> getQuery() {
        return query;
    }

    public void setQuery(List<QueryBean> query) {
        this.query = query;
    }

    public List<CorditionBean> getCordition() {
        return cordition;
    }

    public void setCordition(List<CorditionBean> cordition) {
        this.cordition = cordition;
    }

    public List<List<CommonQueryObjectBean>> getCommonQueryObject() {
        return commonQueryObject;
    }

    public void setCommonQueryObject(List<List<CommonQueryObjectBean>> commonQueryObject) {
        this.commonQueryObject = commonQueryObject;
    }

    public static class IsNEDBean {
        @SerializedName("new")
        private String newX;
        private String edit;
        private String del;

        public String getNewX() {
            return newX;
        }

        public void setNewX(String newX) {
            this.newX = newX;
        }

        public String getEdit() {
            return edit;
        }

        public void setEdit(String edit) {
            this.edit = edit;
        }

        public String getDel() {
            return del;
        }

        public void setDel(String del) {
            this.del = del;
        }
    }

    public static class QueryBean {
        private String id;
        private String lpub;
        private String stitle;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLpub() {
            return lpub;
        }

        public void setLpub(String lpub) {
            this.lpub = lpub;
        }

        public String getStitle() {
            return stitle;
        }

        public void setStitle(String stitle) {
            this.stitle = stitle;
        }
    }

    public static class CorditionBean {
        private String sfield;
        private String scomparison;
        private String svalue;

        public String getSfield() {
            return sfield;
        }

        public void setSfield(String sfield) {
            this.sfield = sfield;
        }

        public String getScomparison() {
            return scomparison;
        }

        public void setScomparison(String scomparison) {
            this.scomparison = scomparison;
        }

        public String getSvalue() {
            return svalue;
        }

        public void setSvalue(String svalue) {
            this.svalue = svalue;
        }
    }

    public static class CommonQueryObjectBean {
        private String key;
        private String value;
        private Object valuename;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Object getValuename() {
            return valuename;
        }

        public void setValuename(Object valuename) {
            this.valuename = valuename;
        }
    }
}
