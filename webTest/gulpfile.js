const gulp = require("gulp"),
  shell = require("shelljs"),
  replace = require("gulp-string-replace"),
  minimist = require("minimist"),
  gulpSequence = require("gulp-sequence"),
  fs = require("fs"),
  GulpSSH = require("gulp-ssh");

const dest = "./dest",
  serverPath = "/opt/tomcat-search/webapps/finchinaAPP/hf/privateFund",
  serverSVN =
    "https://192.168.100.188/svn/jso/finchinaAPP/WebContent/hf/privateFund",
  serverF9SVN = "https://192.168.100.188/svn/jso/finchinaAPP/WebContent/f9",
  svn = "./privateFund";

gulp.task("replaceStr", function(cb) {
  console.log("---------------replaceStr---------------");
  return gulp
    .src(dest + "/assets/js/**/*.js")
    .pipe(
      replace(
        /bjbank_015993|000000|20180517182605_10000000000|20180309181242_13900000001/g,
        ""
      )
    )
    .pipe(gulp.dest("./temp/js"));
  cb();
});

gulp.task("coverFiles", ["replaceStr"], function(cb) {
  console.log("---------------coverFiles---------------");
  shell.rm("-rf", dest + "/assets/js");
  shell.cp("-R", "./temp/js", dest + "/assets/js");
  shell.rm("-rf", "./temp");
  shell.cp("-R", "./*.png", dest);
  cb();
});

gulp.task("default", ["replaceStr", "coverFiles"]);

gulp.task("buildFiles", function() {
  return shell.exec("npm run build");
});

var msgOptions = {
  string: "m",
  default: { m: "DDC-11380" }
};
var options = minimist(process.argv.slice(2), msgOptions);

gulp.task("addFilesToServer", function(cb) {
  var gulpSSH = new GulpSSH({
    ignoreErrors: false,
    sshConfig: {
      host: "10.15.97.42",
      username: "root",
      password: "jsoproject"
    }
  });

  gulpSSH.shell("rm -rf " + serverPath);

  setTimeout(function() {
    gulp.src(dest + "/**").pipe(gulpSSH.dest(serverPath));
    cb();
  }, 1000);
});

gulp.task("updateServerSvn", function(cb) {
  shell.exec("svn checkout " + serverSVN);
  cb();
});

gulp.task("checkInServerSvn", function() {
  let arr = serverSVN.split("/"),
    category = arr[arr.length - 1];
  shell.cd(category);
  shell.rm("-rf", "*");
  shell.cd("..");
  // shell.rm('-rf', svn + '/*.html');

  shell.cp("-R", dest + "/*", svn);

  shell.cd(category);
  shell.exec("svn add * --force");
  shell.exec("svn commit -m " + options.m);
  shell.cd("..");
});

gulp.task(
  "build",
  gulpSequence(
    "buildFiles",
    "replaceStr",
    "coverFiles",
    "updateServerSvn",
    "checkInServerSvn",
    "addFilesToServer"
  )
);
