package com.hotnews;

import com.hotnews.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Author: XianDaLi
 * Date: 2020/8/10 3:45
 * Remark:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HotNewsApplication.class)
public class LikeServiceTests {

	@Autowired
	LikeService likeService;
	@Test
	public void testLike(){
		likeService.like(123,1,1);
		Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));

	}

	@Test
	public void testDisLike(){
		likeService.disLike(123,1,1);
		Assert.assertEquals(-1,likeService.getLikeStatus(123,1,1));
	}

	// 初始化数据 (每次测试用例)
	@Before
	public void setUp(){
		System.out.println("setUp");
	}

	// 清理数据  回调 (每次测试用例)
	@After
	public void tearDown(){
		System.out.println("tearDown");
	}

	// 本类运行之前
	@BeforeClass
	public static void beforeClass(){
		System.out.println("beforeClass");
	}

	// 本类运行之后
	@AfterClass
	public static void afterClass(){
		System.out.println("afterClass");
	}
}
