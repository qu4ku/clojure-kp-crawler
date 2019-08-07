(ns clojure-kp-crawler.core
  (:require [net.cgrand.enlive-html :as html]
            [org.httpkit.client :as http]
            [clojure.string :as str]))


(def kp-url "https://knowledgeprotocol.com/")


(defn get-body [url]
  "Returns html body of a page from provided url."
  (html/html-snippet
      (:body @(http/get url {:insecure? true}))))

(def body (get-body kp-url))

; (defn get-headers [body]
;   (map :content (html/select body [:h2.post__title :a :href])))

; (defn select-titles [body]
;   (map :content (html/select body [:h2.post__title :a])))

(defn select-urls [body]
  "Select articles' urls from provided body."
  (map #(get-in % [:attrs :href]) (html/select body [:h2.post__title :a])))

(defn select-medium-urls [hrefs]
  "Return medium posts' urls."
  (filter #(str/includes? % "medium.com" ) hrefs))


(defn print-medium-urls []
  (map println (select-medium-urls (select-urls body))))


(defn -main
  [& args]
  (print-medium-urls))


(-main)
