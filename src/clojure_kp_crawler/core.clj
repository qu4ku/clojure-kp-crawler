(ns clojure-kp-crawler.core
  (:require [net.cgrand.enlive-html :as html]
            [org.httpkit.client :as http]
            [clojure.string :as str]))


(def kp-url "https://knowledgeprotocol.com/")


(defn get-body [url]
  "Returns html body of a page from provided url."
  (html/html-snippet
      (:body @(http/get url {:insecure? true}))))


(defn generate-pages-to-crawl [num-of-pages]
  "Generate pages to crawl from 1 to num-of-pages"
  (let [base "https://knowledgeprotocol.com/?page="
        nums (range 1 (inc num-of-pages))]
    (map #(str base %)
         nums)))

                         
(defn select-urls [body]
  "Select articles' urls from provided body."
  (map #(get-in % [:attrs :href]) 
       (html/select body [:h2.post__title :a])))


(defn select-medium-urls [hrefs]
  "Return medium posts' urls."
  (filter #(str/includes? % "medium.com") 
          hrefs))


(defn get-medium-urls [num-of-pages]
  "Gets medium posts from knowledgeprotocol.
   Gets first num-of-pages posts."
  (map #(select-medium-urls (select-urls (get-body %)))
       (generate-pages-to-crawl num-of-pages)))


(def urls-test (get-medium-urls 10))

(count urls-test)
(map println urls-test)

(defn -main
  [& args]
  (get-medium-urls 10))


(-main)
