package edu.cmu.cs.cs214.hw6.framework.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.cmu.cs.cs214.hw6.plugins.FakeDataPlugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class DataVisualizationFrameworkTest {
    DataVisualizationFrameworkImpl dvframework;
    DataPlugin fakeplugin;
    DataPlugin mockedplugin;

    @BeforeEach
    public void setUp() {
        dvframework = new DataVisualizationFrameworkImpl();
        fakeplugin = new FakeDataPlugin();
        mockedplugin = mock(DataPlugin.class);
    }

    @Test
    public void simpleTest() {
        assertEquals(dvframework.getDataSetName(), "");
        assertEquals(dvframework.getDataSetDescription(), "");
        assertTrue(dvframework.getRegisteredPluginNames().isEmpty());
    }

    @Test
    public void fakeTest() {
        dvframework.registerPlugin(fakeplugin);
        List<String> pluginNames = new ArrayList<>();
        pluginNames.add("name");
        assertEquals(dvframework.getDataSetName(), "name");
        assertEquals(dvframework.getDataSetDescription(), "description");
        assertEquals(dvframework.getRegisteredPluginNames(), pluginNames);
    }

    @Test
    public void mockTest() {
        when(mockedplugin.getDataSetName()).thenReturn("mock plugin");
        when(mockedplugin.getDataSetDescription()).thenReturn("mock plugin description");
        assertEquals(mockedplugin.getDataSetName(), "mock plugin");
        assertEquals(mockedplugin.getDataSetDescription(), "mock plugin description");
    }
}
