package one.socketdemo;

/**
 * Created by Administrator on 2016/5/22.
 */
public class MyMessage {
    String content;
    String name;
    String time;
    boolean selfFlag;

    public void setSelfFlag(boolean selfFlag) {
        this.selfFlag = selfFlag;
    }

    public boolean isSelfFlag() {

        return selfFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MyMessage() {
    }

    public MyMessage(boolean selfFlag, String time, String name, String content) {
        this.selfFlag = selfFlag;
        this.time = time;
        this.name = name;
        this.content = content;
    }
}
