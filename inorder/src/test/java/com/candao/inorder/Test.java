package com.candao.inorder;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({ "classpath*:spring-context-payment.xml" })
public class Test {
//
//	@Autowired
//	private DaoSupport sqlSessionTemplate;
//
//	@Autowired
//	private InorderEmployeeDao employeeDao;
//
//	@Autowired
//	private InorderStationDao stationDao;
//	@Autowired
//	private InorderMenuDao menuDao;

//	@org.junit.Test
//	public void queryListMenu() {
//		List<String> ss = new ArrayList<String>();
//		ss.add("003251");
//		ss.add("003250");
//		ss.add("003249");
//		Map<String, Object> mapList = new HashMap<String, Object>();
//		mapList.put("items", ss);
//		mapList.put("menuNo", 1);
//		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
//		List<TblMenu> menus = menuDao.queryMeun(mapList);
//		System.out.println(menus);
//	}
//
//	@org.junit.Test
//	public void test() {
//		 CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
//		 List<TblEmpdept> list = employeeDao.empdepts(null);
//		// System.out.println(list);
//		// CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
//		// System.out.println("--------------------");
//	}
//
//	@Autowired
//	private InorderCheckTableDao checkTableDao;
//	// @Autowired
//	// private InorderCheckTableService checkTableService;
//
//	@org.junit.Test
//	public void testQueryCheck() {
////		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
////		Map<String, Object> parmap = new HashMap<>();
////		TblCheck tblCheck = checkTableDao.tableInfoMes(parmap);
////		System.out.println(tblCheck);
//	};
//
//	@org.junit.Test
//	public void testAddCheck() {
//		Map<String, String> out = new HashMap<String, String>();
//		TblCheck check = new TblCheck(new Date(), "122", 1, 1, "1", 1);
//		check.setOpentime(new Date());
//		check.setOpenperiod(1);
//		check.setOpenstation(9);
//		check.setOpenstationref(9);
//		check.setOpenemp(12);
//		check.setChecktot(123);
//		check.setItemtot(12);
//		check.setDiscbefore(12.0);
//		check.setDiscafter(12);
//		check.setIspaid(1);
//		check.setVoidMes("0");
//		check.setIsmodified(1);
//		check.setLastmodifiedtime(new Date());
//		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
//		checkTableDao.addCheck(check);
//		System.out.println(out);
//	}
//
//	@Autowired
//	private InorderItemDao itemDao;
//
//	@org.junit.Test
//	public void testAddItem() {
//
//		List<String> ss = new ArrayList<String>();
//		ss.add("003251");
//		ss.add("003250");
//		ss.add("003249");
//		Map<String, Object> mapList = new HashMap<String, Object>();
//		mapList.put("items", ss);
//		mapList.put("menuNo", 1);
//		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
//		List<TblMenu> menus = menuDao.queryMeun(mapList);
//
//		List<TblItem> items = new ArrayList<TblItem>();
//		TblItem tblItem = null;
//		for (int inx = 0; inx < menus.size(); inx++) {
//			TblMenu tblMenu = menus.get(inx);
//			Date date = new Date();
//			tblItem = new TblItem(date, 12345678, 1000, inx + 1, tblMenu.getItem());
//			tblItem.setName3(tblMenu.getName3());
//			tblItem.setNames3(tblMenu.getNames3());
//			tblItem.setPrintq1(tblMenu.getPrintq1());
//			tblItem.setPrintq2(tblMenu.getPrintq2());
//			tblItem.setPrintq3(tblMenu.getPrintq3());
//			tblItem.setPrintq4(tblMenu.getPrintq4());
//			tblItem.setPrintq5(tblMenu.getPrintq5());
//			tblItem.setDept(tblMenu.getDept());
//			tblItem.setCategory(tblMenu.getCategory());
//			tblItem.setClassMes(tblMenu.getClassMes());
//			tblItem.setPrice(tblMenu.getItemprice1());
//			tblItem.setOgnprice(tblMenu.getItemprice1());
//			tblItem.setTotalcost(tblMenu.getTotalcost());
//
//			tblItem.setOrdertime(date);
//			tblItem.setOrderstation(1);
//			tblItem.setOrderemp(999);
//			tblItem.setParentitemidx(1);
//			tblItem.setChildcount(1);
//			tblItem.setType(1);
//			tblItem.setFromtable("");
//			tblItem.setIsprintoncheck("1");
//			tblItem.setPrintstatus(1);
//			tblItem.setPrintcheckstatus("1");
//			tblItem.setPrintedoncheck(1);
//			tblItem.setPricelevel(1);
//			tblItem.setModimethod(1);
//			tblItem.setDiscbefore(1.00);
//			tblItem.setDiscafter(2.00);
//			tblItem.setModitot(100.123);
//			tblItem.setUnitqty(1.00);
//			tblItem.setQty(1.023);
//			tblItem.setItemtot(1.00);
//			tblItem.setRvdiscbefore(1.00);
//			tblItem.setRvdiscafter(0.123);
//			tblItem.setRvmoditot(1.123);
//			tblItem.setRvitemtot(10.12);
//			tblItem.setCheckdisclevel(1);
//			tblItem.setLastmodifiedtime(date);
//			items.add(tblItem);
//		}
//
//		CustomerContextHolder.setCustomerType(DataSourceInstances.DATA_POSSERVER);
//		int j = itemDao.addbatchItem(items);
//		System.out.println(j);
//	};
//
//	@Autowired
//	private InorderPrintQueueDao printQueueDao;
//	@org.junit.Test
//	public void testPrintQueue() {
//		  TblPrintqueue printqueue=printQueueDao.queryPrintQueue("9");
//		  System.out.println(printqueue);
//	}

}
