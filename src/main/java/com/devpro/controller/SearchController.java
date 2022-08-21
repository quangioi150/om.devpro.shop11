package com.devpro.controller;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.devpro.SolveException.CustomException;
import com.devpro.model.ProductFilterModel;
import com.devpro.services.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.devpro.model.AjaxResponse;
import com.devpro.model.ProductSearch;
import com.devpro.model.search;
import com.devpro.services.ProductService;

import com.devpro.entities.Product;

import java.util.List;

@Controller
public class SearchController{

	private static final String CURRENTCATEGORYSEO = null;
	public final static String FILTER_MODEL = "filterModel";

	/**
	 * 
	 * @param model:    trung gian trao đổi thông tin giữa Controller và View(Jsp)
	 * @param request:  Chứa các thông tin Trình Duyệt truy vấn thông qua mẫu:
	 *                  ?abc=xyz
	 * @param response: Dữ liệu trả về Trình Duyệt.
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String index(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		model.addAttribute("search", new search());
		return "front-end/search";
	}

	/**
	 * Ajax /*
	 */
	@Autowired
	ProductService productService;
	@Autowired
	HttpServletRequest request;


	//SEARCH-KEYWORD
//		@RequestMapping(value = { "/product/search" }, method = RequestMethod.POST)
//		public String seachByKeyWord(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response)
//				throws Exception {
//			HttpSession httpSession = request.getSession();
//			ProductSearch productSearch = (ProductSearch) httpSession.getAttribute("productSeach");
//			httpSession.removeAttribute(CURRENTCATEGORYSEO);
//			
//			String keyword = request.getParameter("keyword");
//			productSearch.setKeyword(keyword);
//			
//			Integer currentPage = 1;
//			
//			String strCurrentPage = request.getParameter("page");
//			
//			if(strCurrentPage != null) currentPage = Integer.parseInt(strCurrentPage);
//				productSearch.setCurrentPage(currentPage);
//			
//			productSearch.setCurrentPage(null);
//			httpSession.setAttribute("size", productService.search(productSearch).size());
//			
//			productSearch.setCurrentPage(currentPage);
//			model.addAttribute("products", productService.search(productSearch));
//			return "front-end/search";
//		}

//	@RequestMapping(value = "/product/search", method = RequestMethod.GET)
//	public String shopSearch(Model model) throws Exception{
//		try {
//			HttpSession session = request.getSession();
//			String favorite = request.getParameter("favorite");
//			if (favorite != null && favorite.equals("true")) {
//				String username = (String) session.getAttribute("USER");
//				User user = userRepository.findByUsername(username);
//				List<Product> products = user.getFProducts();
//				model.addAttribute("products", products);
//				model.addAttribute("favorite",true);
//				model.addAttribute("cate", "shop");
//				model.addAttribute("size", products.size());
//				return "front-end/shop";
//			}
//			ProductFilterModel proFilter = null;
//			if (session.getAttribute(FILTER_MODEL) != null) {
//				proFilter = (ProductFilterModel) session.getAttribute(FILTER_MODEL);
//			} else {
//				proFilter = new ProductFilterModel();
//				proFilter.setCurrenPage(1);
//				session.setAttribute(FILTER_MODEL, proFilter);
//			}
//
//			String strCategoryId = request.getParameter("categoryid");
//			String strCollectionId = request.getParameter("collectionid");
//			String strCurrentPage = request.getParameter("page");
//			String strPriceBegin = request.getParameter("priceBegin");
//			String strPriceEnd = request.getParameter("priceEnd");
//			String strSort = request.getParameter("sort");
//			String tag = request.getParameter("tag");
//
//			if (strCategoryId != null) {
//				proFilter.setCategoryId(Integer.valueOf(strCategoryId));
//				proFilter.setSort(null);
//			}
//			if (strCollectionId != null) {
//				proFilter.setCollectionId(Integer.valueOf(strCollectionId));
//				proFilter.setSort(null);
//			}
//			if (strPriceBegin != null) {
//				proFilter.setBeginPrice(Float.valueOf(strPriceBegin));
//				proFilter.setSort(null);
//			}
//			if (strPriceEnd != null)
//				proFilter.setEndPrice(Float.valueOf(strPriceEnd));
//			if (strCurrentPage != null)
//				proFilter.setCurrenPage(Integer.valueOf(strCurrentPage));
//			if (strSort != null)
//				proFilter.setSort(strSort);
//			if (tag != null) {
//				proFilter.resetFilter();
//				proFilter.setTag(tag);
//			}
//
//			List<Product> products = productService.filterProduct(proFilter);
//			model.addAttribute("products", products);
//			model.addAttribute("size", proFilter.getSize());
//			model.addAttribute("totalPage", proFilter.getTotalPage());
//
//			model.addAttribute("collections", webService.getAllCollection());
//			model.addAttribute("categories", webService.getAllCategory());
//			model.addAttribute("tags", webService.getAllTag());
//
//			model.addAttribute("currentPage", proFilter.getCurrenPage());
//			model.addAttribute("currentCategoryId", proFilter.getCategoryId());
//			model.addAttribute("currentCollectionId", proFilter.getCollectionId());
//			model.addAttribute("cate", "shop");
//			model.addAttribute("price",proFilter.getBeginPrice());
//
//			return "front-end/shop";
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CustomException();
//		}
//	}
	@Autowired
	WebService webService;
	@RequestMapping(value = "/product/search", method = RequestMethod.POST)
	public String search(Model model) throws Exception {
		try {
			HttpSession session = request.getSession();
			ProductFilterModel proFilter = null;
			if (session.getAttribute(FILTER_MODEL) != null) {
				proFilter = (ProductFilterModel) session.getAttribute(FILTER_MODEL);
			} else {
				proFilter = new ProductFilterModel();
				session.setAttribute(FILTER_MODEL, proFilter);
			}
			String keyword = request.getParameter("keyword");
			if (keyword != null) {
				proFilter.resetFilter();
				proFilter.setSearchKeyword(keyword);
			}

			List<Product> products = productService.filterProduct(proFilter);
			model.addAttribute("products", products);
			model.addAttribute("size", proFilter.getSize());
			model.addAttribute("totalPage", proFilter.getTotalPage());

			model.addAttribute("collections", webService.getAllCollection());
			model.addAttribute("categories", webService.getAllCategory());
			model.addAttribute("currentPage", proFilter.getCurrenPage());
			model.addAttribute("currentCategoryId", proFilter.getCategoryId());
			model.addAttribute("currentCollectionId", proFilter.getCollectionId());
			model.addAttribute("cate", "shop");

			return "front-end/search";
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException();
		}
	}
}
