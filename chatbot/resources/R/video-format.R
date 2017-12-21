#!/usr/bin/Rscript

# install.packages("RMySQL")
library(RMySQL)

con <- dbConnect(MySQL(), dbname="imdb", user="imdb", password="imdb")
values <- dbGetQuery(con, "select movie_info.info as format, count(*) as freq from movie_info, info_type where info_type.info = 'LD picture format' and info_type_id = info_type.id group by movie_info.info")

invisible(jpeg('/tmp/video-format.jpg'))
barplot(values$freq, names.arg = values$format, horiz=FALSE, cex.names=0.5)
invisible(dev.off())

