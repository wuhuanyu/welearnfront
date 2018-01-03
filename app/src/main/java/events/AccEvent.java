package events;

/**
 * Created by stack on 2018/1/3.
 */

public class AccEvent extends BaseEvent {
    public static final int ACC_LOGIN=1;
    public static final int ACC_SIGNUP=2;
    public int id=-1;
    public AccEvent(int type,int result,String msg){
        this.type=type;
        this.result=result;
        this.msg=msg;
    }

    public AccEvent(int type,int result,int id){
        this.type=type;
        this.result=result;
        this.id=id;
    }
}
