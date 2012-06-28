(function(){var a=Handlebars.template,b=Handlebars.templates=Handlebars.templates||{};b["reports-testrunsummary.html"]=a(function(a,b,c,d,e){function p(a,b){var c="",d;return c+='\n    <h3 id="subtitle3" class="subtitle-left">',d=a.subtitle3,typeof d===l?d=d.call(a,{hash:{}}):d===n&&(d=m.call(a,"subtitle3",{hash:{}})),c+=o(d)+"</h3>\n    ",c}function q(a,b){var c="",d;return c+='\n    <h3 id="subtitle4" class="subtitle-right">',d=a.subtitle4,typeof d===l?d=d.call(a,{hash:{}}):d===n&&(d=m.call(a,"subtitle4",{hash:{}})),c+=o(d)+"</h3>\n    ",c}function r(a,b,c){var d="",e;return d+='\n        <div class="summaryline">\n            <a href="#/reports/testrundetail/',e=c.data,e=e===null||e===undefined||e===!1?e:e.summary,e=e===null||e===undefined||e===!1?e:e.id,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"...data.summary.id",{hash:{}})),d+=o(e)+"?only=",e=a.resultstatus,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"resultstatus",{hash:{}})),d+=o(e)+'" class="summarylinetitle result-status-',e=a.statusclass,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"statusclass",{hash:{}})),d+=o(e)+'">',e=a.resulttype,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"resulttype",{hash:{}})),d+=o(e)+'</a>\n            <div class="summarylinenumber">',e=a.numberoftests,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"numberoftests",{hash:{}})),d+=o(e)+'</div>\n            <div class="summarylinepercent">',e=a.percentageoftotal,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"percentageoftotal",{hash:{}})),d+=o(e)+"</div>\n        </div>\n    ",d}function s(a,b,c){var d="",e;return d+='\n        <div class="summaryline">\n            <a href="#/reports/testrundetail/',e=c.data,e=e===null||e===undefined||e===!1?e:e.summary,e=e===null||e===undefined||e===!1?e:e.id,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"...data.summary.id",{hash:{}})),d+=o(e)+"?only=",e=a.resultstatus,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"resultstatus",{hash:{}})),d+=o(e)+'" class="summarylinetitle result-status-',e=a.statusclass,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"statusclass",{hash:{}})),d+=o(e)+'">',e=a.resulttype,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"resulttype",{hash:{}})),d+=o(e)+'</a>\n            <div class="summarylinenumber">',e=a.numberoftests,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"numberoftests",{hash:{}})),d+=o(e)+'</div>\n            <div class="summarylinepercent">',e=a.percentageoftotal,typeof e===l?e=e.call(a,{hash:{}}):e===n&&(e=m.call(a,"percentageoftotal",{hash:{}})),d+=o(e)+"</div>\n        </div>\n    ",d}c=c||a.helpers;var f="",g,h,i,j,k=this,l="function",m=c.helperMissing,n=void 0,o=this.escapeExpression;f+='<div id="subtitle">\n    <h2 id="subtitle1" class="subtitle-left">',g=b.subtitle1,typeof g===l?g=g.call(b,{hash:{}}):g===n&&(g=m.call(b,"subtitle1",{hash:{}})),f+=o(g)+'</h2>\n	<h2 id="subtitle2" class="subtitle-right">',g=b.subtitle2,typeof g===l?g=g.call(b,{hash:{}}):g===n&&(g=m.call(b,"subtitle2",{hash:{}})),f+=o(g)+'</h2>\n    <div class="clear"></div>\n    ',g=b.subtitle3,h=c["if"],j=k.program(1,p,e),j.hash={},j.fn=j,j.inverse=k.noop,g=h.call(b,g,j);if(g||g===0)f+=g;f+="\n    ",g=b.subtitle4,h=c["if"],j=k.program(3,q,e),j.hash={},j.fn=j,j.inverse=k.noop,g=h.call(b,g,j);if(g||g===0)f+=g;f+='\n    <div class="clear"></div>\n    <h5 id="createdTime" class="subtitle-left">',g=b.timeCreated,typeof g===l?g=g.call(b,{hash:{}}):g===n&&(g=m.call(b,"timeCreated",{hash:{}})),f+=o(g)+'</h5>\n    <div class="clear"></div>\n</div>\n<div id="testrunsummary-chart" class="chart">\n</div>\n<div id="summary">\n    ',g=b.summarylines,h=c.each,j=k.programWithDepth(r,e,b),j.hash={},j.fn=j,j.inverse=k.noop,g=h.call(b,g,j);if(g||g===0)f+=g;f+="\n    <hr />\n    ",g=b.summarytotal,h=c["with"],j=k.programWithDepth(s,e,b),j.hash={},j.fn=j,j.inverse=k.noop,g=h.call(b,g,j);if(g||g===0)f+=g;return f+='\n</div>\n<div class="clear">\n</div>\n<a class="button" href="#/reports/testrundetail/',g=b.data,g=g===null||g===undefined||g===!1?g:g.summary,g=g===null||g===undefined||g===!1?g:g.id,typeof g===l?g=g.call(b,{hash:{}}):g===n&&(g=m.call(b,"data.summary.id",{hash:{}})),f+=o(g)+'">Detailed Results</a>\n<a id="',g=b.data,g=g===null||g===undefined||g===!1?g:g.summary,g=g===null||g===undefined||g===!1?g:g.id,typeof g===l?g=g.call(b,{hash:{}}):g===n&&(g=m.call(b,"data.summary.id",{hash:{}})),f+=o(g)+'/reschedule-FAIL" class="button testrun-reschedule-bulk" href="javascript:;">Reschedule Failed Tests</a>\n<a id="',g=b.data,g=g===null||g===undefined||g===!1?g:g.summary,g=g===null||g===undefined||g===!1?g:g.id,typeof g===l?g=g.call(b,{hash:{}}):g===n&&(g=m.call(b,"data.summary.id",{hash:{}})),f+=o(g)+'/reschedule-BROKEN_TEST" class="button testrun-reschedule-bulk" href="javascript:;">Reschedule Broken Tests</a>\n\n',f})})()