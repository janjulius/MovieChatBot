#!/usr/bin/Rscript

# install.packages("RMySQL")
library(RMySQL)

con <- dbConnect(MySQL(), dbname="imdb", user="imdb", password="imdb")

franceCount <- dbGetQuery(con, "select count(*) as count from genres, countries where genres.genre = 'war' or 'action' or 'horror' or 'crime' AND countries.country = 'France'"
usCount <- dbGetQuery(con, "select count(*) as count from genres, countries where genres.genre = 'war' or 'action' or 'horror' or 'crime' AND countries.country = 'US'"

invisible(jpeg('/tmp/video-format.jpg'))
barplot(values$freq, names.arg = values$format, horiz=FALSE, cex.names=0.5)
invisible(dev.off())
