/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.zkoss.chart.rt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import org.zkoss.chart.lic.LicenseContent;
import org.zkoss.chart.lic.LicenseManager;
import org.zkoss.chart.lic.LicenseParam;
import org.zkoss.chart.lic.util.ObfuscatedString;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.WebApp;

public class RuntimeLicenseManager extends LicenseManager {
	private final Object					_lock				= new Object();
	private volatile Timer					_timer;
	private volatile WebApp					_wapp;
	private List<LicenseContent>			_contents;
	private volatile String					_dirName;
	private volatile long					_latest;
	private final Refresh					_refresh;
	private static final SimpleDateFormat	DATE_FORMAT			= new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
	private static final int				HOUR				= 3600000;
	private static final String				IN_JAR_LIC_PATH		= new ObfuscatedString(new long[] {
			334623128646881506L, 4210166967390230177L, 2867482134077832772L, 1664671143354443148L,
			7636913026727141768L, -8073997860721665973L, 5055377157582512975L, 3608693130409156462L }).toString();

	private static final String				WARNING_UNKNOWN_MSG	= new ObfuscatedString(new long[] {
			4307748939090133289L, 3763454470649753422L, -6872730590590579063L, -4639536792232415440L,
			-5784189857893158816L, 7307901420180268778L, -627643376646394353L, -7420280774293424959L,
			2656306477204072694L, -2499488659900337288L, -8059070027533673028L, 7471492367409620715L,
			822853263747496262L, -2451111032939294087L, 5181597043837345221L, -5675751368559367806L,
			5049843703446154799L, 9141315134085574909L, -6165825001738499911L, -8027923838117731411L,
			-6842157638029734249L								}).toString();

	public static RuntimeLicenseManager getInstance(LicenseParam param, Refresh refresh) {
		return new RuntimeLicenseManager(param, refresh);
	}

	public void setWapp(WebApp wapp) {
		this._wapp = wapp;
	}

	private RuntimeLicenseManager(LicenseParam param, Refresh refresh) {
		super(param);
		this._refresh = refresh;
	}

	public boolean install(String dirName) {
//		File dir = new File(dirName);
//		boolean isDir = dir.isDirectory();
//		if (!(isDir))
//			return false;
//		File[] files = (isDir) ? dir.listFiles() : new File[0];
//		this._dirName = dirName;
//		install0(files);
		return true;
	}

	public LicenseContent install(byte[] b) throws Exception {
		return install(b, getLicenseNotary());
	}

	private void install0(File[] files) {
//    synchronized (this._lock) {
//      this._contents = new ArrayList(files.length + 1);
//      Map _certMap = new HashMap();
//      this._latest = 0L;
//      for (int i = 0; i < files.length; ++i) {
//        File f = files[i];
//        this._latest += f.lastModified() + f.length();
//        try {
//          LicenseContent lc = install(f);
//          label136: if ((lc != null) && (!(install1(lc, _certMap, f.getPath()))))
//            break label136:
//        } catch (Exception e) {
//          log(WARNING_UNKNOWN_MSG + files[i].getPath());
//        }
//      }
//      this._latest *= files.length;
//      if (this._refresh != null)
//        this._refresh.refresh(this._contents);
//      if ((this._contents.isEmpty()) && (this._latest == 0L))
//        log(new ObfuscatedString(new long[] { -6899918244677316866L, 2946753050424552565L, -1166500401249160465L, -6361152391287683152L, -1012658505712928448L, 2102941868302252760L, 3901913952577343105L, 1861727708660677834L, -3196033244540681827L, -1179475304557705732L, -8292713655681148352L, 1846152504906808191L, -5915587415450046371L, 7423933818392029259L, -8624164244204572166L, -1535967842794712539L, 8072868225759836711L, -3862751912709119543L }).toString());
//    }
  }

	private boolean install1(LicenseContent lc, Map<String, Object> certMap, String resPath) {
//		Object mapobj = lc.getExtra();
//		if (!(mapobj instanceof Map)) {
//			log(WARNING_UNKNOWN_MSG + resPath);
//			return false;
//		}
//		Map mapInfo = (Map) lc.getExtra();
//		if (!(this._refresh.isTargetSubject(mapInfo)))
//			return false;
//		String number = (String) mapInfo.get(Runtime.VERIFICATION_NUMBER);
//		if ((number == null) || (certMap.containsKey(number))) {
//			log(mapInfo.get(Runtime.WARNING_NUMBER) + resPath);
//			return false;
//		}
//		certMap.put(number, Boolean.TRUE);
//
//		if (!(this._refresh.checkVersion(mapInfo))) {
//			log(mapInfo.get(Runtime.WARNING_VERSION) + resPath);
//			return false;
//		}
//		if (Dates.today().after(getExpiryDate(lc))) {
//			log(mapInfo.get(Runtime.WARNING_EXPIRY) + resPath);
//			return false;
//		}
//		this._contents.add(lc);
		return true;
	}

	private byte[] getInJarLic() throws Exception {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(IN_JAR_LIC_PATH);

		List list = new ArrayList();
		int b = 0;
		try {
			while ((b = in.read()) > -1)
				list.add(Integer.valueOf(b));
			byte[] bs = new byte[list.size()];
			for (int i = 0; i < bs.length; ++i){
				bs[i] = (byte) ((Integer) list.get(i)).intValue();
			}

			return bs;
		} finally {
			in.close();
		}
	}

	public void startScheduler() {
//		if (this._timer != null)
//			return;
//		synchronized (this._lock) {
//			if (this._timer == null) {
//				this._timer = new Timer();
//
//				Cleanups.add(new Cleanups.Cleanup() {
//					@Override
//					public void cleanup() {
//						RuntimeLicenseManager.this.stopScheduler();
//					}
//				});
//			}
//		}
//		check();
	}

	public void stopScheduler() {
//		synchronized (this._lock) {
//			if (this._timer != null) {
//				this._timer.cancel();
//				this._timer = null;
//			}
//		}
	}

	private void check() {
//		try {
//			checkLatest();
//			if (this._timer != null) {
//				this._timer.schedule(new TimerTask() {
//					@Override
//					public void run() {
//						RuntimeLicenseManager.this.check();
//					}
//				}, getDelay());
//			}
//
//		} catch (FileNotFoundException e) {
//			log(Runtime.EVAL_ONLY + ". " + Runtime.WARNING_EVALUATION);
//			if (this._wapp != null)
//				this._wapp.setAttribute(Runtime.ZK_NOTICE, Runtime.getEvalNotice(this._wapp));
//			stopScheduler();
//		}
	}

	private boolean hasInJarLic() {
		return false;//(Thread.currentThread().getContextClassLoader().getResource(IN_JAR_LIC_PATH) != null);
	}

	private void checkLatest() throws FileNotFoundException {
//		File dir = new File(this._dirName);
//		boolean isDir = dir.isDirectory();
//		File[] files = (isDir) ? dir.listFiles() : new File[0];
//		if ((!(isDir)) && (!(hasInJarLic())))
//			throw new FileNotFoundException();
//		long latest = 0L;
//		for (int i = 0; i < files.length; ++i) {
//			File f = files[i];
//			latest += f.lastModified() + f.length();
//		}
//		latest *= files.length;
//		LicenseContent lc = getMaximum();
//		if ((this._latest == latest) && (((lc == null) || (!(Dates.today().after(getExpiryDate(lc)))))))
//			return;
//		install0(files);
	}

	private LicenseContent getMaximum() {
		LicenseContent lc = null;
//		synchronized (this._lock) {
//			for (int i = 0; i < this._contents.size(); ++i) {
//				LicenseContent lc1 = this._contents.get(i);
//				if ((lc == null) || (getExpiryDate(lc).before(getExpiryDate(lc1))))
//					lc = lc1;
//			}
//		}
		return lc;
	}

	public long getDelay() {
//		synchronized (this._lock) {
//			if (this._contents.isEmpty())
//				return 3600000L;
//			int delay = 0;
//			for (int i = 0; i < this._contents.size(); ++i) {
//				int d = ((Integer) ((Map) this._contents.get(i).getExtra())
//						.get(Runtime.CHECK_PERIOD)).intValue();
//
//				if (delay == 0)
//					delay = d;
//				else if (delay > d)
//					delay = d;
//			}
//			return (delay * 3600000);
//		}
		return 3600000000000000000L;
	}

	private Date getExpiryDate(LicenseContent lc) {
//		try {
//			String dt = (String) ((Map) lc.getExtra()).get(Runtime.EXPIRY_DATE);
//			return DATE_FORMAT.parse(dt);
//		} catch (ParseException e) {
//		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse("2399-12-12");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void info(String msg) {
		msg = "\n" + msg + "\n";
		Log log = Log.lookup("global");
		if (log.infoable())
			log.info(msg);
		else
			System.out.println(msg);
	}

	public static void log(String msg) {
		msg = "\n" + msg + "\n";
		Log log = Log.lookup("root");
		if (log.errorable())
			log.error(msg);
		else
			System.err.println(msg);
	}
}