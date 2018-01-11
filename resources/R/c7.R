#!/usr/bin/Rscript
#author Lars Schipper

install.packages("RMySQL")
install.packages("ggplot2")

library(RMySQL)
library(ggplot2)

con <- dbConnect(MySQL(), dbname="lars_bigmovie", user="lars_bigmovie", password="g5ZaaJv7", host="www.cloudzeker.nl")

#total movie count in france
franceTotalCount <- dbGetQuery(con, "select count(ID) as count
                               from countries
                               where country = 'France'")

#violant movies in france
franceViolanceCount <- dbGetQuery(con, "select count(ID) as count 
                          from genres 
                          WHERE genre = 'war' or 'action' or 'horror' or 'crime'
                          AND ID = (
                            select ID
                            from countries
                            where country = 'France')")

#total movies in usa
usaTotalCount <- dbGetQuery(con, "select count(ID) as count
                          from countries
                          where country = 'USA'")

#voilant movies in usa
usaViolanceCount <- dbGetQuery(con, "select count(ID) as count 
                          from genres 
                          WHERE genre = 'war' or 'action' or 'horror' or 'crime'
                          AND ID = (
                            select ID
                            from countries
                            where country = 'France')")

usaPercentage = usaViolanceCount / usaTotalCount
francePercentage = franceViolanceCount / franceTotalCount

invisible(jpeg('/tmp/video-format.jpg'))
ggplot(mapping=aes(x=dat))+
  geom_bar(data=franceCount$count, aes(x=dat-0.1), fill="red", binwidth=0.1)+
  geom_bar(data=usCount$count, fill="blue", binwidth=0.1)
invisible(dev.off())
