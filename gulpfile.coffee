gulp = require "gulp"
sass = require "gulp-sass"
autoprefixer = require "gulp-autoprefixer"

paths =
  styles: "styles/**/*.scss"
  scss: "styles/styles.scss"

gulp.task "sass", ()->
  return gulp.src paths.scss
    .pipe sass
      errLogToConsole: true
    .pipe autoprefixer
      browsers: "last 2 versions"
      remove: false
      cascade: false
    .pipe gulp.dest "resources/public/styles"

gulp.task "watch", ["sass"], ()->
  gulp.watch paths.styles, ["sass"]
