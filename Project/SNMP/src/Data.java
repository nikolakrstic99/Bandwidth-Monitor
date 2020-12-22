import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ireasoning.protocol.*;
import com.ireasoning.protocol.snmp.*;

public class Data implements Runnable {

	private static final String R1 = "192.168.10.1", R2 = "192.168.20.1", R3 = "192.168.30.1";

	private static final int IF_DESCR = 1;
	private static final int IF_IN_OCTETS = 9;
	private static final int IF_IN_PACKETS = 10;
	private static final int IF_OUT_OCTETS = 15;
	private static final int IF_OUT_PACKETS = 16;
	private static final int SNMP_PORT = 161;
	private static final String SNMP_USERNAME = "si2019";
	private static final String SNMP_PASSWORD = "si2019";
	private static final int SIZE = 10;
	private Long startInPackets, startOutPackets, startInOctets, startOutOctets;
	private int curSize, from;
	private SnmpTarget target;
	private SnmpSession session;
	private Thread thread;
	private SnmpTableModel table;
	List<Long> times = new ArrayList<Long>();
	List<Long> inPackets = new ArrayList<Long>();
	List<Long> outPackets = new ArrayList<Long>();
	List<Long> inOctets = new ArrayList<Long>();
	List<Long> outOctets = new ArrayList<Long>();
	List<Double> inBandwidth = new ArrayList<Double>();
	List<Double> outBandwidth = new ArrayList<Double>();
	private String ip, port;
	private GraphPanel gp;
	private boolean end = false;

	public Data(String router, String port, GraphPanel gp) {
		super();
		if (router.equals("R1"))
			ip = R1;
		else if (router.equals("R2"))
			ip = R2;
		else
			ip = R3;

		this.gp = gp;
		this.port = port;

		curSize = 0;
		update();
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		end = true;
	}

	@Override
	public void run() {
		while (!end) {
			target = new SnmpTarget(ip, SNMP_PORT, SNMP_USERNAME, SNMP_PASSWORD, SnmpConst.SNMPV2);
			try {
				session = new SnmpSession(target);
				session.loadMib2();
				table = session.snmpGetTable("ifTable");

				SnmpVarBind descr[] = table.getColumn(IF_DESCR);

				int i = 0;
				for (i = 0; i < descr.length; i++)
					if (descr[i].getValue().toString().equals(port))
						break;

				String stringToConvert;

				if (curSize > 0) {

					stringToConvert = String.valueOf(table.getValueAt(i, IF_IN_PACKETS));
					inPackets.add(Long.parseLong(stringToConvert) - startInPackets);
					System.out.print("ulazni paketi:");
					System.out.println(stringToConvert);

					stringToConvert = String.valueOf(table.getValueAt(i, IF_OUT_PACKETS));
					outPackets.add(Long.parseLong(stringToConvert) - startOutPackets);
					System.out.print("izlazni paketi");
					System.out.println(stringToConvert);
					
					stringToConvert = String.valueOf(table.getValueAt(i, IF_IN_OCTETS));
					inOctets.add(Long.parseLong(stringToConvert));
					System.out.print("ulazni bajtovi:");
					System.out.println(stringToConvert);

					stringToConvert = String.valueOf(table.getValueAt(i, IF_OUT_OCTETS));
					outOctets.add(Long.parseLong(stringToConvert));
					System.out.print("izlazni bajtovi");
					System.out.println(stringToConvert);
					System.out.println();

					// protok
					int listSize = inBandwidth.size();
					Double tmp = (double) (inOctets.get(listSize) - inOctets.get(listSize - 1));
					inBandwidth.add(8 * tmp / 10);
					tmp = (double) (outOctets.get(listSize) - outOctets.get(listSize - 1));
					outBandwidth.add(8 * tmp / 10);
					times.add(times.get(listSize - 1) + 1);
				} else {
					stringToConvert = String.valueOf(table.getValueAt(i, IF_IN_PACKETS));
					startInPackets = Long.parseLong(stringToConvert);
					System.out.print("pocetni ulazni paketi:");
					System.out.println(stringToConvert);

					stringToConvert = String.valueOf(table.getValueAt(i, IF_OUT_PACKETS));
					startOutPackets = Long.parseLong(stringToConvert);
					System.out.print("pocetni izlazni paketi:");
					System.out.println(stringToConvert);

					stringToConvert = String.valueOf(table.getValueAt(i, IF_IN_OCTETS));
					startInOctets = Long.parseLong(stringToConvert);
					inOctets.add(startInOctets);
					System.out.print("pocetni ulazni bajtovi:");
					System.out.println(stringToConvert);

					stringToConvert = String.valueOf(table.getValueAt(i, IF_OUT_OCTETS));
					startOutOctets = Long.parseLong(stringToConvert);
					outOctets.add(startOutOctets);
					System.out.print("pocetni izlazni bajtovi");
					System.out.println(stringToConvert);
					System.out.println();
					
					inPackets.add((long) 0);
					outPackets.add((long) 0);

					inBandwidth.add((double) 0);
					outBandwidth.add((double) 0);
					times.add((long) 0);
				}

				curSize++;
				if (curSize > 10)
					from = 1;
				else
					from = 0;
				int to = Math.min(curSize, SIZE + 1);
				inPackets = inPackets.subList(from, to);
				outPackets = outPackets.subList(from, to);
				
				inOctets=inOctets.subList(from, to);
				outOctets=outOctets.subList(from, to);
				
				inBandwidth = inBandwidth.subList(from, to);
				outBandwidth = outBandwidth.subList(from, to);
				times = times.subList(from, to);

				gp.update();
				thread.sleep(10000, 0);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void update() {

	}

	public int from() {
		return from;
	}

	public int to() {
		return curSize - SIZE;
	}

	public List<Long> getTimes() {
		return times;
	}

	public List<Long> getInPackets() {
		return inPackets;
	}

	public List<Long> getOutPackets() {
		return outPackets;
	}

	public List<Double> getInBandwidth() {
		return inBandwidth;
	}

	public List<Double> getOutBandwidth() {
		return outBandwidth;
	}

	public void setTimes(List<Long> times) {
		this.times = times;
	}

	public void setInPackets(List<Long> inPackets) {
		this.inPackets = inPackets;
	}

	public void setOutPackets(List<Long> outPackets) {
		this.outPackets = outPackets;
	}

	public void setInBandwith(List<Double> inBandwith) {
		this.inBandwidth = inBandwith;
	}

	public void setOutBandwith(List<Double> outBandwith) {
		this.outBandwidth = outBandwith;
	}
}
