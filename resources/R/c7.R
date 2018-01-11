#!/usr/bin/Rscript

#install.packages("RMySQL")
#install.packages("ggplot2")

library(RMySQL)
library(ggplot2)

con <- dbConnect(MySQL(), dbname="imdb", user="imdb", password="imdb")

franceCount <- dbGetQuery(con, "select count(*) as count from genres, countries where genres.genre = 'war' or 'action' or 'horror' or 'crime' AND countries.country = 'France'"
usCount <- dbGetQuery(con, "select count(*) as count from genres, countries where genres.genre = 'war' or 'action' or 'horror' or 'crime' AND countries.country = 'US'"

invisible(jpeg('/tmp/video-format.jpg'))
ggplot(mapping=aes(x=dat))+
  geom_bar(data=franceCount$count, aes(x=dat-0.1), fill="red", binwidth=0.1)+
  geom_bar(data=usCount$count, fill="blue", binwidth=0.1)
invisible(dev.off())
