/* 
 * Registration of Manage Tests Pages.
 */

Pages.addGroup("testmgmt", "Manage Tests", true);
Pages.group("testmgmt").registerPage("viewproject", "View Project", true);
Pages.group("testmgmt").registerPage("addbuild", "Add Build", true);
Pages.group("testmgmt").registerPage("addrelease", "Add Release", true);
Pages.group("testmgmt").registerPage("addtest", "Add Test Case", true).comingSoon();
Pages.group("testmgmt").registerPage("findtests", "Find Test Cases", true).comingSoon();
