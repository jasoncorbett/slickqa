/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 4/12/12
 * Time: 4:00 PM
 */

var AboutSlickPage = SlickPage.extend({
    group: "about",
    codename: "slick",
    name: "About Slick",

    initialize: function() {
        this.on("finish", this.onFinish, this);
    },

    onFinish: function() {
        $(".slick-logo").on("click", function() {
            $(".slick-logo").attr("src", $(".slick-logo").attr("src"));
        });
    }
});
