(function(){var a=Handlebars.template,b=Handlebars.templates=Handlebars.templates||{};b["runtests-scheduletp.html"]=a(function(a,b,c,d,e){function o(a,b){var c="",d;return c+='\n                    <option value="',d=a.id,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"id",{hash:{}})),c+=n(d)+'" data-environmentname="',d=a.name,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"name",{hash:{}})),c+=n(d)+'">',d=a.name,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"name",{hash:{}})),c+=n(d)+"</option>\n                ",c}function p(a,b){var c="",d;return c+='\n                    <option data-name="',d=a.name,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"name",{hash:{}})),c+=n(d)+'" value="',d=a.id,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"id",{hash:{}})),c+=n(d)+'">',d=a.name,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"name",{hash:{}})),c+=n(d)+"</option>\n                ",c}function q(a,b){var c="",d;return c+='\n                    <option data-name="',d=a.name,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"name",{hash:{}})),c+=n(d)+'" value="',d=a.id,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"id",{hash:{}})),c+=n(d)+'">',d=a.name,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"name",{hash:{}})),c+=n(d)+"</option>\n                ",c}c=c||a.helpers;var f="",g,h,i,j=this,k="function",l=c.helperMissing,m=void 0,n=this.escapeExpression;f+='<div id="schedule-testplan-box" class="box width-8">\n    <div id="schedult-testplan-box-name" class="boxname">Schedule Test Plan \'',g=b.data,g=g===null||g===undefined||g===!1?g:g.tp,g=g===null||g===undefined||g===!1?g:g.name,typeof g===k?g=g.call(b,{hash:{}}):g===m&&(g=l.call(b,"data.tp.name",{hash:{}})),f+=n(g)+'\'</div>\n    <span id="schedule-testplan-box-explanation" class="explanation">Submitting this form will create empty results for all ',g=b.data,g=g===null||g===undefined||g===!1?g:g.testcount,typeof g===k?g=g.call(b,{hash:{}}):g===m&&(g=l.call(b,"data.testcount",{hash:{}})),f+=n(g)+' tests in the plan.</span>\n    <hr class="glowing" />\n    <form action="?">\n        \n        <div class="formline">\n            <label for="schedule-testplan-environment-select">Environment\n                <span class="explanation">The environment to test with</span>\n            </label>\n            <select id="schedule-testplan-environment-select">\n                ',g=b.data,g=g===null||g===undefined||g===!1?g:g.config,h=c.each,i=j.program(1,o,e),i.hash={},i.fn=i,i.inverse=j.noop,g=h.call(b,g,i);if(g||g===0)f+=g;f+='\n            </select>\n        </div>\n\n        <div class="formline">\n            <label for="schedule-testplan-release-select">Release\n            </label>\n            <select id="schedule-testplan-release-select">\n                ',g=b.data,g=g===null||g===undefined||g===!1?g:g.proj,g=g===null||g===undefined||g===!1?g:g.releases,h=c.each,i=j.program(3,p,e),i.hash={},i.fn=i,i.inverse=j.noop,g=h.call(b,g,i);if(g||g===0)f+=g;f+='\n            </select>\n        </div>\n        \n        <div class="formline">\n            <label for="schedule-testplan-release-build-select">Build\n                <span class="explanation">The release and build to test with</span>\n            </label>\n            <select id="schedule-testplan-release-build-select">\n                ',g=b.data,g=g===null||g===undefined||g===!1?g:g.builds,h=c.each,i=j.program(5,q,e),i.hash={},i.fn=i,i.inverse=j.noop,g=h.call(b,g,i);if(g||g===0)f+=g;return f+='\n            </select>\n        </div>\n                \n         \n        \n        <div class="formline">\n            <label for="schedule-testplan-config">Configuration Value\n                <span class="explanation">The configuration value</span>\n            </label>\n            <input type="text" name="schedule-testplan-config" id="schedule-testplan-config" />\n        </div>\n        \n        <div class="formline">\n            <label for="schedule-testplan-config-value">Configuration\n                <span class="explanation">The configuration key</span>\n            </label>\n            <input type="text" name="schedule-testplan-config-value" id="schedule-testplan-config-value" />\n        </div>\n        \n        <div class="formline">\n            <a href="javascript:;" class="button" id="schedule-testplan-form-submit">Schedule</a>\n        </div>\n\n    </form>\n</div>\n<div class="clear">\n</div>\n',f})})()