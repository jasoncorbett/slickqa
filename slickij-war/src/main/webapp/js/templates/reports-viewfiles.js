(function(){var a=Handlebars.template,b=Handlebars.templates=Handlebars.templates||{};b["reports-viewfiles.html"]=a(function(a,b,c,d,e){function o(a,b){var d="",e,f;d+='\n<div class="clear"></div>\n<div class="boxname">File Viewer:</div>\n<div class="box">\n    <div class="filelist">\n        ',e=a.files,f=c.each,i=j.program(2,p,b),i.hash={},i.fn=i,i.inverse=j.noop,e=f.call(a,e,i);if(e||e===0)d+=e;return d+='\n    </div>\n    <div class="filedisplay">\n        <img src="/images/htmlicon.png" alt="test"/>\n    </div>\n</div>\n\n',d}function p(a,b){var c="",d;return c+='\n            <div class="fileicon">\n                <img class="file box" src="/images/htmlicon.png" alt="',d=a.filename,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"filename",{hash:{}})),c+=n(d)+'"/>\n                <span class="label filename">',d=a.filename,typeof d===k?d=d.call(a,{hash:{}}):d===m&&(d=l.call(a,"filename",{hash:{}})),c+=n(d)+"</span>\n            </div>\n        ",c}c=c||a.helpers;var f="",g,h,i,j=this,k="function",l=c.helperMissing,m=void 0,n=this.escapeExpression;g=b.result,h=c["with"],i=j.program(1,o,e),i.hash={},i.fn=i,i.inverse=j.noop,g=h.call(b,g,i);if(g||g===0)f+=g;return f+="\n",f})})()