(defproject geoservices "0.0.1"
  :description "Simple Spatial Services, apply JTS over GEOJSON"
  :url "http://github.com/diogok/simple-spatial-services"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main flora-connect.server
  :ring { :handler geoservices.server/app
          :init geoservices.server/start
          :destroy geoservices.server/stop
          :reload-paths ["src"] }
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [ring "1.3.0"]
                 [ring/ring-jetty-adapter "1.3.0"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-http "0.9.0"]
                 [diogok/cljts "0.4.1"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :plugins [[lein-ring "0.8.11"]
                             [lein-midje "3.1.3"]]}}
  )
