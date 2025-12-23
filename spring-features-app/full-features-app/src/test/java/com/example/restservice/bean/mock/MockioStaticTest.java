package com.example.restservice.bean.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @className: MockioStaticTest
 * @author: geeker
 * @date: 11/27/25 6:11 PM
 * @Version: 1.0
 * @description:
 */

@RunWith(MockitoJUnitRunner.class)
//@ExtendWith(MockitoExtension.class)
public class MockioStaticTest {
    
    @Test
    public void useageTest() {
        List listMock = Mockito.mock(List.class);
        listMock.add("one");

        System.out.println(listMock.size());
        listMock.clear();

        Mockito.verify(listMock).add("one");
        System.out.println(listMock.size());

        Mockito.verify(listMock).clear();

    }


    @Test
    public void stubTest() {

        List listMock = Mockito.mock(List.class);
        when(listMock.size()).thenReturn(10);
        System.out.println(listMock.size());
    }

    @Mock
    List listMockAnnotation; /*= Mockito.mock(List.class);*/
    @Test
    public void annotationTest(){
        listMockAnnotation.add("one");
        System.out.println(listMockAnnotation.size());

        when(listMockAnnotation.size()).thenReturn(10);
        System.out.println(listMockAnnotation.size());

        listMockAnnotation.add("one");
        System.out.println(listMockAnnotation.size());
    }


    @Spy
    List<String> listSpy = new ArrayList<String>();
    @Test
    public void spyTest(){
        listSpy.add("one");
        System.out.println(listSpy.size());

        verify(listSpy).add("one");
        System.out.println(listSpy.size());

        when(listSpy.size()).thenReturn(10);
        System.out.println(listSpy.size());

        listSpy.add("one");
        System.out.println(listSpy.size());
    }


    @Test
    public void whenNotUseSpyAnnotation_thenCorrect() {
        List<String> spyList = Mockito.spy(new ArrayList<String>());

        spyList.add("one");
        spyList.add("two");
        System.out.println(spyList.size());

        Mockito.verify(spyList).add("one");
        Mockito.verify(spyList).add("two");

        assertEquals(2, spyList.size());
        System.out.println(spyList.size());

        Mockito.doReturn(100).when(spyList).size();
        assertEquals(100, spyList.size());
        System.out.println(spyList.size());
    }


    @Test
    public void verifyExplanationTest() {
        List<String> listSpy = spy(new ArrayList<>());

        // 实际执行添加操作
        listSpy.add("one");
        listSpy.add("two");
        listSpy.add("three");  // 再次添加 "one"
        listSpy.add("three");  // 再次添加 "one"

        System.out.println("实际列表内容: " + listSpy);  // [one, two, one]
        System.out.println("实际大小: " + listSpy.size());  // 3

        // verify 只是验证调用，不会重新添加元素
        verify(listSpy).add("one");        // 验证 add("one") 至少被调用1次
        verify(listSpy, times(1)).add("one");  // 验证 add("one") 被调用2次
        verify(listSpy, times(2)).add("three");  // 验证 add("one") 被调用2次
        verify(listSpy).add("two");        // 验证 add("two") 被调用1次
        verify(listSpy, times(4)).add(anyString());  // 验证总共调用了3次add

        // 验证后列表状态不变
        System.out.println("验证后列表内容: " + listSpy);  // [one, two, one] - 不变
        System.out.println("验证后大小: " + listSpy.size());  // 3 - 不变
    }
}
