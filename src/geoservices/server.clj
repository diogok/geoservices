(ns geoservices.server
  (:use ring.adapter.jetty
        geoservices.web-wrap
        [compojure.core :only [defroutes GET POST context]]
        [compojure.route :only [resources]]
        [compojure.handler :only [site]]
        [ring.util.response :only [redirect]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defn start [] nil)
(defn stop [] nil)

(defroutes main

  (GET "/" []
    (redirect "/index.html"))

  (context "/api/v1" [] 
    (GET "/foo" []
      "bar")
         )

  (resources "/"))

(def app
  (-> (handler/site main)
      (wrap-context)
      (wrap-context-redir)
      (wrap-proxy-redir)
      (wrap-jsonp)
      (wrap-options)))

(defn -main
  ""
  [& args]
  (start)
  (run-jetty app {:port 3000 :join? true})
  (stop))
