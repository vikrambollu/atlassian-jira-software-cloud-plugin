package com.atlassian.jira.cloud.jenkins.buildinfo.freestyle;

import com.atlassian.jira.cloud.jenkins.buildinfo.service.JiraBuildInfoRequest;
import com.atlassian.jira.cloud.jenkins.common.factory.JiraSenderFactory;
import com.atlassian.jira.cloud.jenkins.common.response.JiraSendInfoResponse;
import com.atlassian.jira.cloud.jenkins.config.JiraCloudPluginConfig;
import com.atlassian.jira.cloud.jenkins.config.JiraCloudSiteConfig;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.ListBoxModel;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class TestPostBuildStep extends Recorder implements Serializable, SimpleBuildStep {

    private String site;

    @DataBoundConstructor
    public TestPostBuildStep(String site) {
        this.site = site;
        // Empty constructor
    }

    @Nullable
    public String getSite() {
        return site;
    }

    @DataBoundSetter
    public void setSite(final String site) {
        this.site = site;
    }


//    @Override
//    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
//
//
//    }


    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        return super.perform(build, launcher, listener);
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        final JiraBuildInfoRequest request =
                new JiraBuildInfoRequest(this.getSite(), "", run);

        final JiraSendInfoResponse response =
                JiraSenderFactory.getInstance().getJiraBuildInfoSender().sendBuildInfo(request);

        //  logResult(taskListener, response);

        // return response;

        //return true;
    }


    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Inject
        private transient JiraCloudPluginConfig globalConfig;

        @Override
        public String getDisplayName() {
            return "Send build information to Jira";
        }

        @Override
        public boolean isApplicable(Class aClass) {
            return true;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            return super.configure(req, json);
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillSiteItems() {
            ListBoxModel items = new ListBoxModel();
            final List<JiraCloudSiteConfig> siteList = globalConfig.getSites();
            for (JiraCloudSiteConfig siteConfig : siteList) {
                items.add(siteConfig.getSite(), siteConfig.getSite());
            }

            return items;
        }
    }



}