import java.io.IOException;
import com.ireasoning.protocol.*;
import com.ireasoning.protocol.snmp.*;

public class Zadatak implements Runnable {


	private Thread thread;
	private String community="si2019",host1="192.168.10.1",host2="192.168.20.1",host3="192.168.30.1";
	private int port=161,first,second,third,sum,sumOld;
	private SnmpTarget target1,target2,target3;
	private SnmpSession session1,session2,session3;
	private SnmpTableModel table1,table2,table3;
	
	public Zadatak() {
		target1=new SnmpTarget(host1,port,community,community,SnmpConst.SNMPV2);
		target2=new SnmpTarget(host2,port,community,community,SnmpConst.SNMPV2);
		target3=new SnmpTarget(host3,port,community,community,SnmpConst.SNMPV2);
		try {
			session1=new SnmpSession(target1);
			session2=new SnmpSession(target2);
			session3=new SnmpSession(target3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SnmpSession.loadMib2();
		thread=new Thread(this);
		thread.start();
	}
	

	@Override
	public void run() {
		
		while(!thread.isInterrupted()) {
		
			try {
				table1=session1.snmpGetTable("ifTable");
				table2=session2.snmpGetTable("ifTable");
				table3=session3.snmpGetTable("ifTable");
				first=table1.getColumn(10)[0].getValue().BITS + table1.getColumn(16)[0].getValue().BITS;
				second=table2.getColumn(10)[0].getValue().BITS + table1.getColumn(16)[0].getValue().BITS;
				third=table3.getColumn(10)[0].getValue().BITS + table1.getColumn(16)[0].getValue().BITS;
				
				sum=first+second+third;
				System.out.println(sum);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			try {
				sumOld=sum;
				thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		session1.close();
		session2.close();
		session3.close();
		
	}

}
