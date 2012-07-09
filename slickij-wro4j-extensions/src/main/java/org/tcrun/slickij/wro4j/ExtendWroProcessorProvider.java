package org.tcrun.slickij.wro4j;

import ro.isdc.wro.model.resource.processor.ResourcePostProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.decorator.LazyProcessorDecorator;
import ro.isdc.wro.model.resource.processor.support.ProcessorProvider;
import ro.isdc.wro.util.LazyInitializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 7/6/12
 * Time: 7:12 PM
 */
public class ExtendWroProcessorProvider implements ProcessorProvider
{
    @Override
    public Map<String, ResourcePreProcessor> providePreProcessors()
    {
        HashMap<String, ResourcePreProcessor> retval = new HashMap<String, ResourcePreProcessor>();
        retval.put("namedHandlebarsJs", new LazyProcessorDecorator(new LazyInitializer<ResourcePreProcessor>() {

            @Override
            protected ResourcePreProcessor initialize() {
                return new NamedHandlebarsProcessor();
            }
        }));
        return retval;

    }

    @Override
    public Map<String, ResourcePostProcessor> providePostProcessors()
    {
        return new HashMap<String, ResourcePostProcessor>(); // we don't have any
    }
}
