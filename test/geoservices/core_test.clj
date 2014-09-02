(ns geoservices.core-test
  (:use geoservices.core)
  (:use midje.sweet))

(fact "Can work with geojson"
  (let [json "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[20,10]}}"
        json2 "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[20,10]}}]}"]
    (from-geojson json) => (point (c 20 10))
    (to-geojson (point (c 20 10))) => json
    (from-geojson json2) => [ (point (c 20 10)) ]  
    (to-geojson [(point (c 20 10))]) => json2
  ))

(fact "Can make a convex-hull from points"
  (let [p0 (point (c 10 20))
        p1 (point (c -10 -20))
        p2 (point (c 5 5))
        p3 (point (c -15 -20))
        p4 (point (c 2 2))]
    (to-geojson (convex-hull [p0 p1 p2 p3 p4])) 
      => "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-15,-20],[10,20],[5,5],[-10,-20],[-15,-20]]]}}"
    ))

(fact "Can calculate area"
  (let [pol (from-geojson "{\"type\":\"Feature\",\"geometry\": {\"type\":\"Polygon\",\"coordinates\":[[[-15,-20],[10,20],[5,5],[-10,-20],[-15,-20]]]} }")]
    (area pol) => 150.0
    (area-in-meters pol) => (roughly 2.1837E12)
    ))

(fact "Buffers"
  (let [p0 (point (c 10 10))
        buffer (buffer p0 20)
        buffer2 (buffer-in-meters p0 20000)]
    (area buffer) => (roughly 1248.0)
    (area buffer2) => (roughly 0.103)
    ))

 (fact "Union"
  (let [b1 (buffer (point (c 10 10)) 10)
        b2 (buffer (point (c 20 20)) 10)
        b3 (buffer (point (c 30 30)) 10)]
    (union b1 b2) => (union b1 b2)
    (union b1 b2 b3) => (union b1 b2 b3)
    ))

