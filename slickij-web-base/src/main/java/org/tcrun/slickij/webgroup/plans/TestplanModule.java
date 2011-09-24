package org.tcrun.slickij.webgroup.plans;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.tcrun.slickij.webbase.ActionGroup;
import org.tcrun.slickij.webbase.UrlRegistration;

/**
 *
 * @author lhigginson
 */
public class TestplanModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        Multibinder<ActionGroup>     agbinder = Multibinder.newSetBinder(binder(), ActionGroup.class);
        Multibinder<TestplanAction>  rabinder = Multibinder.newSetBinder(binder(), TestplanAction.class);
        Multibinder<UrlRegistration> rbinder  = Multibinder.newSetBinder(binder(), UrlRegistration.class);

        agbinder.addBinding().to(TestplanGroup.class);

        rabinder.addBinding().toInstance(ScheduleTestPlanPage.getTestplanAction());
        rabinder.addBinding().toInstance(ViewTestPlansPage.getTestplanAction());

        rbinder.addBinding().toInstance(ScheduleTestPlanPage.getUrlRegistration());
        rbinder.addBinding().toInstance(ScheduleTestPlanPage.getUrlRegistration());

    }
}
