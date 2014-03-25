package edu.virginia.psyc.pi.mvc;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/24/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class SiteControllerTest {

    private static final String FORWARDED_URL = "/home.html";
    private static final String VIEW = "home";

    MockMvc mockMvc;

    @InjectMocks
    SiteController controller = new SiteController();

//    @Mock
//    MenuService menuService;

//    @Mock
//    Basket basket;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMvc = standaloneSetup(controller)
                .setViewResolvers(viewResolver())
                .build();

//        when(menuService.requestAllMenuItems(any(RequestAllMenuItemsEvent.class))).thenReturn(allMenuItems());

    }

    private InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void rootUrlPopulatesViewModel() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(model().size(1))
                .andExpect(model().attribute("username", "Dan"));
    }

    @Test
    public void rootUrlforwardsCorrectly() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW))
                .andExpect(forwardedUrl(FORWARDED_URL));

    }

}
