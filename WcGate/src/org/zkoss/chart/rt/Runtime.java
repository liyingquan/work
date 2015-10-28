/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.zkoss.chart.rt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.security.auth.x500.X500Principal;

import org.zkoss.chart.lic.CipherParam;
import org.zkoss.chart.lic.KeyStoreParam;
import org.zkoss.chart.lic.LicenseContent;
import org.zkoss.chart.lic.LicenseParam;
import org.zkoss.chart.lic.util.ObfuscatedString;
import org.zkoss.io.Files;
import org.zkoss.lang.Library;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.DesktopInit;
import org.zkoss.zk.ui.util.EventInterceptor;
import org.zkoss.zk.ui.util.ExecutionInit;
import org.zkoss.zul.Window;

public class Runtime {
	static final String							ACTIVE_CODE					= new ObfuscatedString(new long[] {
			2927261568102559820L, 5003116366233426486L, -19977163214018644L }).toString();
	static final String							LICENSE_SUBJECT				= new ObfuscatedString(new long[] {
			3902422651215371349L, 8624385350820619417L, -642393240400619653L }).toString();
	static final String							UP_TIME						= new ObfuscatedString(new long[] {
			7735875781470822017L, 7449797441543358756L						}).toString();
	static final String							SESSION_COUNT				= new ObfuscatedString(new long[] {
			3229559844559651902L, 4507837234696327046L, 7727269190283665611L }).toString();
	static final String							USER_NAME					= new ObfuscatedString(new long[] {
			-7156956517562311570L, -4569457626895068567L, -9130454531809692735L }).toString();
	static final String							COMPANY_ID					= new ObfuscatedString(new long[] {
			-3429614419320347732L, 5284083613633888871L, -2962259370126176133L }).toString();
	static final String							COMPANY_UNIT				= new ObfuscatedString(new long[] {
			4535911707703337843L, -5339041739265159617L, -8006648055853745059L }).toString();
	static final String							COMPANY_NAME				= new ObfuscatedString(new long[] {
			-7340139527016707886L, -5203243759892677212L, 8714822623115369524L }).toString();
	static final String							COMPANY_CITY				= new ObfuscatedString(new long[] {
			-6925611054058810462L, 2143563004714040551L, 1994584055053707741L }).toString();
	static final String							COMPANY_ADDRESS				= new ObfuscatedString(new long[] {
			-8691848786421899489L, -4370996558863340632L, -2709933490946981238L }).toString();
	static final String							COMPANY_ZIPCODE				= new ObfuscatedString(new long[] {
			-4934501068656857753L, -6913828373145765012L, 1063193634233753528L }).toString();
	static final String							COUNTRY						= new ObfuscatedString(new long[] {
			-4504969373906269801L, 5248137891553083335L					}).toString();
	static final String							PROJECT_NAME				= new ObfuscatedString(new long[] {
			830623621279886032L, 459360910074040759L, 4178520520701877821L	}).toString();
	static final String							PRODUCT_NAME				= new ObfuscatedString(new long[] {
			5504882338648710617L, -4761731334749763195L, 996375918662338065L }).toString();
	static final String							PACKAGE						= new ObfuscatedString(new long[] {
			-8439029564924938530L, -4878278112849633009L					}).toString();
	static final String							VERSION						= new ObfuscatedString(new long[] {
			-4847689528984584834L, 2493253216426408014L					}).toString();
	static final String							ISSUE_DATE					= new ObfuscatedString(new long[] {
			-4228764154858292882L, -6898004159031332466L, 6328666951570048917L }).toString();
	static final String							EXPIRY_DATE					= new ObfuscatedString(new long[] {
			-7233890858958970371L, -3423973165832030856L, -5810612950970282077L }).toString();
	static final String							TERM						= new ObfuscatedString(new long[] {
			-6725301182108235475L, -8691110408124621856L					}).toString();
	static final String							VERIFICATION_NUMBER			= new ObfuscatedString(new long[] {
			3823288740853721680L, -6436340937747658512L, 891079956768101415L, 751513662528431611L }).toString();
	static final String							INFORMATION					= new ObfuscatedString(new long[] {
			378870925371295609L, 1418863983102047429L, -5017007170548372422L }).toString();
	static final String							KEY_SIGNATURE				= new ObfuscatedString(new long[] {
			-2573177027008659676L, 5066716785755217927L, 5769746383701090690L }).toString();
	static final String							CHECK_PERIOD				= new ObfuscatedString(new long[] {
			-2439022525501632135L, 6139476070014855270L, -297657911147084449L }).toString();
	static final String							LICENSE_VERSION				= new ObfuscatedString(new long[] {
			-7080462743270045357L, 6928867389785115158L, -154565539896996742L }).toString();
	static final String							WARNING_EXPIRY				= new ObfuscatedString(new long[] {
			-2088056424898980973L, -3616911578495445651L, -8353968737700076168L }).toString();
	static final String							WARNING_PACKAGE				= new ObfuscatedString(new long[] {
			7436618834759965309L, 8220698497085578148L, -6394078374620879850L }).toString();
	static final String							WARNING_VERSION				= new ObfuscatedString(new long[] {
			7417971821667979026L, 7464186339852802771L, 7986314911006223431L }).toString();
	static final String							WARNING_COUNT				= new ObfuscatedString(new long[] {
			-1510608780643214737L, 6313704540210276937L, 4115504365890483558L }).toString();
	static final String							WARNING_NUMBER				= new ObfuscatedString(new long[] {
			8367990676393660796L, -7163797910637480555L, -8349581027556623805L }).toString();

	public static final String					WARNING_EVALUATION			= new ObfuscatedString(
																					new long[] { -1686724076192452649L,
			5479413577522293516L, -4532440330805899180L, -588520627841543404L, 8456291859372871184L,
			1401357791436041375L, 268356434517717358L, 6851517267893501802L, 8322528570082373914L,
			-6486603437729459692L, 6492517821599687597L, 5954164131745707670L, -4688046615305172193L,
			-3253650542118144799L, -3942995649046487754L, -3006398607329210499L, 4377000184919845598L,
			-6044641037340879455L, -6545226114933324288L, -4054034874690456467L, -1752483163428305138L,
			-7595134214129916055L, -1687651324948594427L, -5993057464344102883L, -5084557572444328712L,
			-2361125655604470497L, -3141397762767198376L, -9041239724621675548L, 3386525571404563517L,
			401141833494134873L, 4001161498275622503L, -750118547428158894L, 1680991499245255296L }).toString();

	static final String							ZKCHARTS					= new ObfuscatedString(new long[] {
			7687308498378989512L, 3363155612692898862L, -4411539046136182894L }).toString();
	private static final String					PUB_STORE					= new ObfuscatedString(new long[] {
			-467598638084582726L, 6535119678915292211L, -5761942064796554413L, -2140330853237991898L }).toString();

	private static final String					ALIAS						= new ObfuscatedString(new long[] {
			-2751357802016299199L, 4066217211348802619L, -2064662869185498634L, 4043793295118034741L,
			6227175189710674534L											}).toString();
	private static final String					STORE_PASS					= new ObfuscatedString(new long[] {
			-8677088790027852212L, 4602056908258993522L, 8019503246186939872L, -1944004741470673738L,
			-7033589056015316549L											}).toString();

	private static final ThreadLocal<Boolean>	_pass						= new ThreadLocal();
	private static boolean						_ck;
	private static LicenseParam					_licenseParam				= new LicenseParam() {
																				@Override
																				public String getSubject() {
																					return Runtime.ZKCHARTS;
																				}

																				@Override
																				public Preferences getPreferences() {
																					return null;
																				}

																				@Override
																				public KeyStoreParam getKeyStoreParam() {
																					return Runtime._keystoreParam;
																				}

																				@Override
																				public CipherParam getCipherParam() {
																					return Runtime._cipherParam;
																				}
																			};

	private static KeyStoreParam				_keystoreParam				= new KeyStoreParam() {
																				@Override
																				public InputStream getStream()
																						throws IOException {
																					InputStream in = Thread
																							.currentThread()
																							.getContextClassLoader()
																							.getResourceAsStream(
																									Runtime.PUB_STORE);

																					if (in == null)
																						throw new FileNotFoundException(
																								Runtime.PUB_STORE);
																					return in;
																				}

																				@Override
																				public String getAlias() {
																					return Runtime.ALIAS;
																				}

																				@Override
																				public String getStorePwd() {
																					return Runtime.STORE_PASS;
																				}

																				@Override
																				public String getKeyPwd() {
																					return null;
																				}
																			};

	private static CipherParam					_cipherParam				= new CipherParam() {
																				@Override
																				public String getKeyPwd() {
																					return new ObfuscatedString(
																							new long[] {
			-9017617134232705315L, -3067316756544620689L, -7174741455541659722L, 9223059116147577819L,
			-7389013047307896124L															}).toString();
																				}
																			};

	private static final RuntimeLicenseManager	_licManager					= RuntimeLicenseManager.getInstance(
																					_licenseParam, new Refresh() {
																						@Override
																						public boolean checkVersion(
																								Map<String, Object> map) {
																							String version = (String) map
																									.get(Runtime.VERSION);
																							return ((Strings
																									.isBlank(version)) || ("1.1.0"
																									.startsWith(version)));
																						}

																						@Override
																						public boolean isTargetSubject(
																								Map<String, Object> map) {
																							String subject = (String) map
																									.get(Runtime.LICENSE_SUBJECT);
																							return Runtime.ZKCHARTS
																									.equals(subject);
																						}

																						@Override
																						public Object refresh(
																								List<LicenseContent> contents) {
//																							Runtime.access$500().init2(
//																									contents);
//																							printInfo(contents);
																							return null;
																						}

																						private void printInfo(
																								List<LicenseContent> contents) {
																							for (int i = 0; i < contents
																									.size(); ++i) {
																								LicenseContent lc = contents
																										.get(i);
																								Object mapObj = lc
																										.getExtra();
																								if (mapObj instanceof Map) {
																									Map map = (Map) mapObj;
																									if (isTargetSubject(map))
																										info((String) map
																												.get(Runtime.INFORMATION));
																								}
																							}
																						}

																						private void info(String msg) {
																							RuntimeLicenseManager
																									.info(msg);
																						}
																					});
	private static WebApp						_wapp;
	private static final String					V0							= new ObfuscatedString(new long[] {
			6780048183396145217L, -3514785424911510459L					}).toString();
	private static final String					V1							= new ObfuscatedString(new long[] {
			-8556922120573852888L, 708543790670807158L						}).toString();
	private static final String					MD5STR						= new ObfuscatedString(new long[] {
			-5121064899839768052L, 3334273322282769989L					}).toString();

	public static final String					EVAL_ONLY					= new ObfuscatedString(new long[] {
			8330515038476062730L, -435229286498387605L, -3853053404258091660L }).toString();
	static final String							UNIVERSAL_ACTIVE_CODE		= new ObfuscatedString(new long[] {
			-1128797753529339020L, -733110981657037353L, 4491611533734142717L, -1250450028158659062L,
			-1301458517696305400L, -5349951335757801720L					}).toString();
	static final String							ZK_NOTICE					= new ObfuscatedString(new long[] {
			-8347842002405430398L, 3574087500300642980L, -6273607823570371127L, -5524402949239665762L }).toString();

	static final String							LICENSE_DIRECTORY_PROPERTY	= new ObfuscatedString(new long[] {
			6736365164940027295L, -1302475928255385955L, -7342664994090783108L, 4397684651610525166L,
			-3739603956555900055L, -6887437132615268471L					}).toString();
	private static final String					DEFAULT_LICENSE_DIRECTORY	= new ObfuscatedString(new long[] {
			7798525131307045020L, 9120904666555939754L, -7964755973142850032L, -422867193761513395L }).toString();

	private static final String read(String path) {
		try {
			InputStream is = Runtime.class.getResourceAsStream("/metainfo/chart/" + path);
			if (is != null)
				return new String(Files.readAll(is));
		} catch (Throwable ex) {
		}
		return null;
	}

	public static final boolean init(WebApp wapp, boolean ck) {
		boolean b = true;
//		if ((ck) && (!(_ck))) {
//			_ck = true;
//			boolean charts = "ZK Charts".equals(read("zkcharts"));
//
//			boolean init = true;
//			boolean hasLicenseFieOrFolder = (Library.getProperty(LICENSE_DIRECTORY_PROPERTY) != null)
//					|| (Runtime.class.getResource(DEFAULT_LICENSE_DIRECTORY) != null);
//
//			if ((charts) && (!(hasLicenseFieOrFolder))) {
//				init = false;
//			}
//
//			if (init) {
//				wapp.setAttribute(ACTIVE_CODE, getActiveCode());
//				try {
//					b = init1(wapp);
//					wapp.getConfiguration().addListener(Init.class);
//					b = (b) && (_pass.get().booleanValue());
//					_pass.remove();
//				} catch (Exception e) {
//					b = false;
//				}
//
//			}
//
//			if (charts) {
//				wapp = null;
//			}
//			if ((!(b)) && (wapp != null))
//				wapp.setAttribute(ZK_NOTICE, "ZK_NOTICEZK_NOTICEZK_NOTICEZK_NOTICEZK_NOTICE");
//		}
		return b;
	}

//	private static final String getActiveCode() {
//		Preferences pref = _licenseParam.getPreferences();
//		if (pref != null) {
//			long leastv = pref.getLong(V0, 0L);
//			long mostv = pref.getLong(V1, 0L);
//			if ((leastv == 0L) && (mostv == 0L)) {
//				UUID uuid = UUID.randomUUID();
//				long most = uuid.getMostSignificantBits();
//				long least = uuid.getLeastSignificantBits();
//				pref.putLong(V0, least);
//				pref.putLong(V1, most);
//				if ((least == pref.getLong(V0, 0L)) && (most == pref.getLong(V1, 0L)))
//					return uuidToMD5(most, least);
//			} else {
//				return uuidToMD5(mostv, leastv);
//			}
//		}
//		return null;
//	}

//	private static final String uuidToMD5(long most, long least) {
//		String hostname = null;
//		try {
//			Enumeration nis = NetworkInterface.getNetworkInterfaces();
//
//			while (nis.hasMoreElements()) {
//				NetworkInterface ni = (NetworkInterface) nis.nextElement();
//				if ((ni.isUp()) && (!(ni.isVirtual()))) {
//					Enumeration ias = ni.getInetAddresses();
//					while (ias.hasMoreElements()) {
//						InetAddress ia = (InetAddress) ias.nextElement();
//						hostname = ia.getHostName();
//						if (!("localhost".equals(hostname)))
//							break;
//						hostname = null;
//					}
//
//					if (hostname != null)
//						break;
//				}
//			}
//		} catch (SocketException e1) {
//		}
//		try {
//			long mostsalt = -8556922120573852888L;
//			long leastsalt = -7110043422976597898L;
//			if (hostname == null)
//				hostname = "ZK Host";
//			byte[] hostbytes = hostname.getBytes("UTF-8");
//			long host = 0L;
//			for (int j = 0; (j < hostbytes.length) && (j < 8); ++j)
//				host |= (hostbytes[j] & 0xFF) << j * 8;
//			leastsalt ^= host;
//			if (hostbytes.length > 8) {
//				host = 0L;
//				int j = 0;
//				for (int len = hostbytes.length - 8; (j < len) && (j < 8); ++j)
//					host |= (hostbytes[(j + 8)] & 0xFF) << j * 8;
//				mostsalt ^= host;
//			}
//			String uuidStr = new UUID(most ^ mostsalt, least ^ leastsalt).toString();
//			MessageDigest digest = MessageDigest.getInstance(MD5STR);
//			if (digest != null) {
//				digest.reset();
//				digest.update(uuidStr.getBytes("UTF-8"));
//				byte[] digested = digest.digest();
//				BigInteger bigInt = new BigInteger(1, digested);
//				String result = bigInt.toString(36);
//				StringBuffer sb = new StringBuffer(32);
//				for (int j = 0; j < result.length(); ++j) {
//					if ((j > 0) && (j % 5 == 0))
//						sb.append("-");
//					sb.append(result.charAt(j));
//				}
//				return sb.toString().toUpperCase();
//			}
//		} catch (UnsupportedEncodingException e) {
//		} catch (NoSuchAlgorithmException e) {
//		}
//		return null;
//	}

	static String getEvalNotice(WebApp wapp) {
		StringBuffer sb = new StringBuffer();
		Object o = wapp.getAttribute(ZK_NOTICE);
		if (o != null)
			sb.append(o);
		sb.append(" -->\n").append("<!-- ").append(ZKCHARTS).append(" ").append("1.1.0").append(" ");

		sb.append(EVAL_ONLY);
		return sb.toString();
	}

	private static final boolean init1(WebApp wapp) {
//		_wapp = wapp;
//		String dir = Library.getProperty(LICENSE_DIRECTORY_PROPERTY);
//		boolean isScheduled = false;
//		if (dir != null) {
//			isScheduled = _licManager.install(dir);
//		} else {
//			URL url = Runtime.class.getResource(DEFAULT_LICENSE_DIRECTORY);
//			if (url != null) {
//				isScheduled = _licManager.install(url.getFile());
//			}
//		}
//		if (isScheduled) {
//			_licManager.setWapp(wapp);
//			_licManager.startScheduler();
//			return true;
//		}
//		init1().init2(null);
//		return false;
		return true;
	}

	private static Init1 init1() {
		return Init1._init1;
	}

	public static final void token(Object exec, Object exec2) {
		token0(exec, exec2);
	}

	public static final boolean token(Object exec) {
		return token0(exec);
	}

	private static final boolean token0(Object exec) {
//		if (exec instanceof Execution)
//			return (((Execution) exec).getAttribute(token1(exec)) != null);
//		if (exec instanceof Session) {
//			return (((Session) exec).getAttribute(token1(exec)) != null);
//		}
//		return false;
		return true;
	}

	private static final void token0(Object exec, Object exec2) {
//		if (exec instanceof Execution)
//			((Execution) exec2).setAttribute(token1(exec), Boolean.TRUE);
//		else if (exec instanceof Session)
//			((Session) exec2).setAttribute(token1(exec), Boolean.TRUE);
	}

//	private static final String token1(Object exec) {
//		String plaintext = "mgws"
//				+ ((exec instanceof Desktop) ? ((Desktop) exec).getId()
//						: (exec instanceof Execution) ? ((Execution) exec).getDesktop().getId() : ((Session) exec)
//								.getRemoteAddr()) + "wysb";
//
//		Execution exec0 = Executions.getCurrent();
//		if (exec0 != null) {
//			Desktop desktop = exec0.getDesktop();
//			MessageDigest digest = (MessageDigest) desktop.getAttribute("md_" + desktop.getId());
//
//			if (digest != null) {
//				digest.reset();
//				try {
//					digest.update(plaintext.getBytes("UTF-8"));
//				} catch (UnsupportedEncodingException e) {
//				}
//				byte[] digested = digest.digest();
//				BigInteger bigInt = new BigInteger(1, digested);
//				return bigInt.toString(36);
//			}
//		}
//		return plaintext;
//	}

	private static int stringHash(String value) {
		if (value == null)
			return 0;
		int off = 0;
		int h = 0;
		int len = value.length();
		for (int i = 0; i < len; ++i)
			h = 31 * h + value.charAt(off++);
		return h;
	}

	public static long getCheckSum(RuntimeInfo result) {
		long checksum = 0L;

		X500Principal hobj = result.getHolder();
		checksum += stringHash(hobj.getName());

		X500Principal iobj = result.getIssuer();
		checksum += stringHash(iobj.getName());

		int count = result.getConsumerAmount();
		checksum += count;
		String type = result.getConsumerType();
		checksum += stringHash(type);
		String info = result.getInfo();
		checksum += stringHash(info);

		Date issued = result.getIssued();
		checksum += issued.getTime();

		Date start = result.getNotBefore();
		checksum += ((start == null) ? 0L : start.getTime());
		Date end = result.getNotAfter();
		checksum += ((end == null) ? 0L : end.getTime());

		String subject = result.getSubject();
		checksum += stringHash(subject);

		String licId = result.getLicId();
		checksum += stringHash(licId);

		String licVer = result.getLicVer();
		checksum += stringHash(licVer);

		String userId = result.getUserId();
		checksum += stringHash(userId);

		String userName = result.getUserName();
		checksum += stringHash(userName);

		String companyId = result.getCompanyId();
		checksum += stringHash(companyId);

		String companyName = result.getCompanyName();
		checksum += stringHash(companyName);

		long sessionCount = result.getSessionCount().longValue();
		checksum += sessionCount;

		String zkchartsVer = result.getZkChartsVer();
		checksum += stringHash(zkchartsVer);
		return checksum;
	}

	static final class LicenseEvent extends Event {
		private static final long	serialVersionUID	= 201011241551L;

		private LicenseEvent(Event event) {
			super("onLicense", event.getTarget());
		}
	}

	private static abstract class Init0 implements ExecutionInit, DesktopInit, DesktopCleanup, EventInterceptor {
		private Init0() {
			init1();
		}

		private final void init0(Desktop desktop, Object request) throws Exception {
//			Runtime.Init1.access$2600(init1(), desktop, request);
		}

		private final void cleanup0(Desktop desktop) throws Exception {
//			Runtime.Init1.access$2700(init1(), desktop);
		}

		private final void init2(Execution exec, Execution parent) throws Exception {
//			Runtime.Init1.access$2800(init1(), exec, parent);
		}

		private final Runtime.Init1 init1() {
			return null;//Runtime.Init1.access$700();
		}

		private final void afterProcessEvent0(Event event) {
//			Runtime.Init1.access$2900(init1(), event);
		}

		private final Event beforePostEvent0(Event event) {
			return null;//Runtime.Init1.access$3000(init1(), event);
		}

		private final Event beforeProcessEvent0(Event event) {
			return null;//Runtime.Init1.access$3100(init1(), event);
		}

		private final Event beforeSendEvent0(Event event) {
			return null;//Runtime.Init1.access$3200(init1(), event);
		}
	}

	public static final class Init extends Runtime.Init0 {
		public Init() {
			//super(null);
		}

		@Override
		public void init(Desktop desktop, Object request) throws Exception {
			super.init0(desktop, request);
		}

		@Override
		public void cleanup(Desktop desktop) throws Exception {
			super.cleanup0(desktop);
		}

		@Override
		public void init(Execution exec, Execution parent) throws Exception {
			super.init2(exec, parent);
		}

		@Override
		public void afterProcessEvent(Event event) {
			//super.afterProcessEvent0(event);
		}

		@Override
		public Event beforePostEvent(Event event) {
			return super.beforePostEvent0(event);
		}

		@Override
		public Event beforeProcessEvent(Event event) {
			return super.beforeProcessEvent0(event);
		}

		@Override
		public Event beforeSendEvent(Event event) {
			return super.beforeSendEvent0(event);
		}
	}

	private static final class Init2 {
		private static final BigInteger	_pkey				= new BigInteger("123");
		private static final String		EVAL_LIC_ID			= new ObfuscatedString(new long[] { -2799362229550136139L,
																	-8991339801923478651L, 2054766831045471090L })
																	.toString();
		private static final String		EVAL_LIC_VER		= new ObfuscatedString(new long[] { 4515837249784318634L,
																	-483965117545718936L }).toString();
		private static final String		EVAL_USER_NAME		= new ObfuscatedString(new long[] { 6700787382705499563L,
																	2359114071391082446L, 1949693015831576717L })
																	.toString();
		private static final String		EVAL_COMPANY_ID		= new ObfuscatedString(new long[] { 8997629646135421336L,
																	-7459597106373371040L, 3324566901651534592L })
																	.toString();
		private static final String		EVAL_COMPANY_NAME	= new ObfuscatedString(new long[] { -7105953592593293452L,
																	-8116579615415252408L, -3117127378177258899L,
																	9036340616621476990L }).toString();
		private SimpleDateFormat		_dateFormat			= new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
		private Map<String, Object>		_keys;
		private Object					_licid;
		private Object					_licversion;
		private Object					_username;
		private Object					_companyid;
		private Object					_companyname;
		private Object					_validbegin;
		private Object					_validend;
		private Object					_zkchartsversion;
		private Object					_uptime;

		private Init2() {
			this._keys = new HashMap();
		}

		private Date getNotBefore(Map map) {
			try {
				return this._dateFormat.parse((String) map.get(Runtime.ISSUE_DATE));
			} catch (ParseException e) {
			}
			return new Date(9223372036854775807L);
		}

		private Date getExpiryDate(Map map) {
			try {
				return this._dateFormat.parse((String) map.get(Runtime.EXPIRY_DATE));
			} catch (ParseException e) {
			}
			return new Date(-9223372036854775808L);
		}

		private long getUpTime(Map<String, Object> map) {
//			Long utime = (Long) map.get(Runtime.UP_TIME);
//			return ((utime == null) ? 0L : utime.longValue());
			return 3600000000000000000L;
		}

		private void init2(List<LicenseContent> contents) {
//			if (contents == null) {
//				installEval();
//				return;
//			}
//			Date today = Dates.today();
//			boolean valid = false;
//			Date mindate = new Date(9223372036854775807L);
//			Date maxdate = new Date(-9223372036854775808L);
//			Date maxIssueDate = new Date(-9223372036854775808L);
//			long uptime = 0L;
//			String machine = (String) Runtime._wapp.getAttribute(Runtime.ACTIVE_CODE);
//			for (LicenseContent lc : contents) {
//				Object mapobj = lc.getExtra();
//				if (!(mapobj instanceof Map))
//					continue;
//				Map map = (Map) mapobj;
//				Date bdate = getNotBefore(map);
//				Date edate = getExpiryDate(map);
//				if (today.before(bdate))
//					continue;
//				if (today.after(edate))
//					continue;
//				if (bdate.before(mindate))
//					mindate = bdate;
//				if (edate.after(maxdate))
//					maxdate = edate;
//				String activecode = (String) map.get(Runtime.ACTIVE_CODE);
//				Date issueDate = lc.getIssued();
//				if ((issueDate.after(maxIssueDate))
//						&& (((Runtime.UNIVERSAL_ACTIVE_CODE.equals(activecode)) || ((machine != null) && (machine
//								.equals(activecode)))))) {
//					valid = true;
//					maxIssueDate = issueDate;
//					this._zkchartsversion = map.get(Runtime.VERSION);
//					this._username = map.get(Runtime.USER_NAME);
//					this._companyid = map.get(Runtime.COMPANY_ID);
//					this._companyname = map.get(Runtime.COMPANY_NAME);
//					this._keys = map;
//					this._licid = map.get(Runtime.VERIFICATION_NUMBER);
//					this._licversion = map.get(Runtime.LICENSE_VERSION);
//					long utime = getUpTime(map);
//					if (uptime < utime)
//						uptime = utime;
//				}
//			}
//			if (valid) {
//				this._validbegin = mindate;
//				this._validend = maxdate;
//				this._uptime = Long.valueOf((uptime <= 0L) ? Long.valueOf(
//						maxdate.getTime() - mindate.getTime() + new Random(new Date().getTime()).nextInt(1800000))
//						.longValue() : uptime);
//			}
//
//			if ((!(valid)) || (!(validateLicenseFile())))
//				installEval();
		}

//		private final void installEval() {
//			this._username = EVAL_USER_NAME;
//			this._companyid = EVAL_COMPANY_ID;
//			this._companyname = EVAL_COMPANY_NAME;
//			this._validbegin = new Date();
//
//			this._uptime = Long.valueOf(43200000 + new Random(new Date().getTime()).nextInt(1800000));
//
//			this._validend = new Date(((Date) this._validbegin).getTime() + ((Long) this._uptime).longValue()
//					+ 3600000L);
//
//			this._keys = new HashMap();
//			this._keys.put(Runtime.COMPANY_ID, this._companyid);
//		}

//		private final boolean validateLicenseFile() {
//			return ((this._licid != null) && (this._licversion != null) && (this._username != null)
//					&& (this._companyid != null) && (this._companyname != null) && (this._validbegin != null)
//					&& (this._validend != null) && (this._zkchartsversion != null) && (this._uptime != null));
//		}
//
//		private final String encode(byte[] lic) {
//			try {
//				return new String(lic, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//			}
//			return "";
//		}
//
//		private final byte[] decryption(byte[] lic) {
//			return lic;
//		}
	}

	private static final class Init1 {
		private static final int	FREQ			= 3;
		private static final String	WIN				= new ObfuscatedString(new long[] { 5321153595986820916L,
															6399668227042605266L, -5602491487233658872L,
															2066377234886365691L, 203726272017906087L,
															-3409963818121804467L, -290525610284193589L,
															5360349214446654186L, 7126235572874868104L,
															6443646263932522040L, -2888496393462639896L }).toString()
															+ new ObfuscatedString(new long[] { 8156116825536686571L,
																	-7933011286180755110L, 8429279178335537173L,
																	5140564363110783572L, -4587992236270359981L,
																	-1850871503295801036L, -7368992273418428298L,
																	-9139769631895394533L, 901440478462063480L,
																	1371905052599390164L, -5107498036593385759L,
																	-5563648405980080527L, 2015340681356193425L,
																	916270472538173639L, 7391668993988648096L,
																	5028903049363742715L, -2699427368479864385L,
																	-2350362590362555178L }).toString();

		private static final Init1	_init1			= new Init1();
		private boolean				_stoplicense;
		private boolean				_stopuptime;
		private int					_inModal;
		private final long			_uptime			= new Date().getTime();
		private final Runtime.Init2	_init2=null;
		private static final String	MD5STR			= new ObfuscatedString(new long[] { -5121064899839768052L,
															3334273322282769989L }).toString();
		private static final String	MD				= new ObfuscatedString(new long[] { 2345590606122015185L,
															-5069608179148319545L }).toString();

		private static final String	TITLE			= new ObfuscatedString(new long[] { 6290325967589686282L,
															5925669707962544096L }).toString();
		private static final String	BORDER			= new ObfuscatedString(new long[] { 1517003640222100403L,
															-5881717338632855477L }).toString();
		private static final String	MODE			= new ObfuscatedString(new long[] { 2063230648775789892L,
															-7765346573662599107L, -3183841938540764871L }).toString();
		private static final String	WIDTH			= new ObfuscatedString(new long[] { -2028303217529921622L,
															6478417485293582031L }).toString();
		private static final String	HEIGHT			= new ObfuscatedString(new long[] { 2974696421778732584L,
															-3701582788618461277L }).toString();
		private static final String	ARG				= new ObfuscatedString(new long[] { 3689490926743112102L,
															-8583085698292004623L }).toString();

		private static final String	ZKCHARTS_LOCK	= new ObfuscatedString(new long[] { -4361949641064810920L,
															4760308304985465928L, -7475577516307733383L,
															-3805436675224286611L, 5232398363181540904L }).toString();

		private static final String	UPTIME_EXP		= new ObfuscatedString(new long[] { 2943335330455776128L,
															-4848668867510060470L, 6602582347860445238L,
															-7236343006869602260L, 7976610064108166951L,
															-6993221016480970242L, 6472492144499702799L }).toString();

		private static final String	TRIAL_EXP		= new ObfuscatedString(new long[] { 1520189818443795303L,
															5930700064994299126L, 1008791950702918034L,
															-3953667963320530015L, 3514689977611942643L,
															7420440463667609306L }).toString();

		private static final String	UPTIME_INFO		= new ObfuscatedString(new long[] { -140629977948033438L,
															8411039821423448906L, -3474650949739896324L,
															2772224587032503194L }).toString();
		private static final String	LIC_INFO		= new ObfuscatedString(new long[] { -1327671782532263310L,
															840490956016971870L, 3061901067660749900L }).toString();
		private static final String	MASK_HEAD		= new ObfuscatedString(new long[] { 2610432342261686015L,
															2813741495629532862L, -1181939021078443460L,
															-7375419926652952589L }).toString();
		private static final String	MASK_BODY		= new ObfuscatedString(new long[] { -7924049674768767620L,
															-5640807885409979664L, 7029203202109863500L,
															8437967762489256364L, -309579538571070286L }).toString();

		private static final String	MASK_FOOT		= new ObfuscatedString(new long[] { 812264288056794049L,
															-2332289316366425421L }).toString();

//		private Init1() {
//			this._init2 = new Runtime.Init2(null);
//		}

		private void init2(List<LicenseContent> contents) {
//			this._init2.init2(contents);
//			Runtime._pass.set(Boolean.valueOf(!(this._init2._keys.isEmpty())));
		}

		private final void afterProcessEvent0(Event event) {
//			if (validate(event))
//				return;
//			this._inModal += 1;
		}

		private final Event beforePostEvent0(Event event) {
			return event;
		}

		private final Event beforeProcessEvent0(Event event) {
			Component comp = event.getTarget();
//			if (comp instanceof Charts) {
//				if (this._stopuptime) {
//					maskPvtUptime(comp.getUuid());
//					return null;
//				}
//				if (this._stoplicense) {
//					maskPvtLicense(comp.getUuid());
//					return null;
//				}
//			}
			return event;
		}

		private final Event beforeSendEvent0(Event event) {
			return event;
		}

		private final void init0(Desktop desktop, Object request) {
//			try {
//				MessageDigest digest = MessageDigest.getInstance(MD5STR);
//				desktop.setAttribute(MD + desktop.getId(), digest);
//			} catch (NoSuchAlgorithmException e) {
//			}
//			Runtime.Init init = new Runtime.Init();
//			desktop.setAttribute(Runtime.access$1200(desktop), init);
//			desktop.addListener(init);
		}

		private final void cleanup0(Desktop desktop) {
//			Object init = desktop.getAttribute(Runtime.access$1200(desktop));
//			desktop.removeListener(init);
		}

		private final void init2(Execution exec, Execution parent) {
//			Runtime.access$1300(exec, exec);
		}

		private final boolean validate(Event event) {
			return true;//((!(validUptime(event))) || (!(validLicense(event))));
		}

		private final boolean validLicense(Event event) {
//			if (this._init2._keys.isEmpty()) {
//				complainLicense(event);
//				return false;
//			}
//
//			long time = new Date().getTime();
//			if ((((Date) this._init2._validbegin).getTime() > time)
//					|| (time > ((Date) this._init2._validend).getTime())) {
//				complainLicense(event);
//				return false;
//			}
			return true;
		}

		private final boolean validUptime(Event event) {
//			if ((this._init2._keys.isEmpty()) || (this._init2._uptime == null)) {
//				complainLicense(event);
//				return false;
//			}
//
//			long time = new Date().getTime();
//			if ((this._uptime > time) || (time > this._uptime + ((Long) this._init2._uptime).longValue())) {
//				complainUptime(event);
//				return false;
//			}
			return true;
		}

		private final void showWin(Page pg, String message) {
			Window win = new Window(TITLE, BORDER, true);
			Library.setProperty(ZKCHARTS_LOCK, null);
			win.setMode(MODE);
			win.setWidth(WIDTH);
			win.setHeight(HEIGHT);
			win.setPage(pg);
			Map arg = new HashMap();
			arg.put(ARG, message);
			Component[] comps = Executions.getCurrent().createComponentsDirectly(WIN, "zul", arg);
			for (int j = 0; j < comps.length; ++j)
				comps[j].setParent(win);
		}

		private final void complainLicense(Event event) {
//			this._inModal += 1;
//			if ((!(this._stoplicense)) && ((this._inModal & 0x3) == 0)) {
//				Component comp = event.getTarget();
//				if (comp instanceof Charts) {
//					Page pg = comp.getPage();
//					if (pg != null) {
//						showWin(pg, TRIAL_EXP);
//						this._stoplicense = true;
//						maskPvtLicense(comp.getUuid());
//					}
//				}
//			}
		}

		private final void complainUptime(Event event) {
//			this._inModal += 1;
//			if ((!(this._stopuptime)) && ((this._inModal & 0x3) == 0)) {
//				Component comp = event.getTarget();
//				if (comp instanceof Charts) {
//					Page pg = comp.getPage();
//					if (pg != null) {
//						showWin(pg, UPTIME_EXP);
//						this._stopuptime = true;
//						maskPvtUptime(comp.getUuid());
//					}
//				}
//			}
		}

//		private final void maskPvtUptime(String uuid) {
//			maskPvt(uuid, UPTIME_INFO);
//		}
//
//		private final void maskPvtLicense(String uuid) {
//			maskPvt(uuid, LIC_INFO);
//		}

//		private final void maskPvt(String uuid, String txt) {
//			Clients.evalJavaScript(MASK_HEAD + uuid + MASK_BODY + txt + MASK_FOOT);
//		}
	}
}