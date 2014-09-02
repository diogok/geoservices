(ns geoservices.core-test
  (:use geoservices.core)
  (:use midje.sweet))

(fact "Can work with geojson"
  (let [json "{\"type\":\"Point\",\"coordinates\":[20,10]}"]
    (to-geojson (from-geojson json)) => json))

(fact "Can make a convex-hull from points"
  (let [p0 (point (c 10 20))
        p1 (point (c -10 -20))
        p2 (point (c 5 5))
        p3 (point (c -15 -20))
        p4 (point (c 2 2))]
    (to-geojson (convex-hull [p0 p1 p2 p3 p4])) 
      => "{\"type\":\"Polygon\",\"coordinates\":[[[-15,-20],[10,20],[5,5],[-10,-20],[-15,-20]]]}"
    ))

(fact "Can calculate area"
  (let [pol (from-geojson "{\"type\":\"Polygon\",\"coordinates\":[[[-15,-20],[10,20],[5,5],[-10,-20],[-15,-20]]]}")]
    (area pol) => 150.0
    ))

(fact "Buffers"
  (let [p0 (point (c 10 10))
        buffer (buffer p0 20)
        buffer2 (buffer-in-meters p0 20000)]
    (int (area buffer)) => 1248
    (int (* 1000 (area buffer2) ) ) => 103
    ))

