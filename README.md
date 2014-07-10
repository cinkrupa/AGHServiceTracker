AGHServiceTracker
=================


# NFC

The application was tested using MIFARE NTAG203 tags

To write data to NFC tags you can use NFC Tools:
https://play.google.com/store/apps/details?id=com.wakdev.wdnfc

##Data format:

* Record0 - Media : `application/pl.edu.agh.servicetracker`, data (json): `{"item_id":1}`

NFC Tools: Add a record -> Data -> Content-type: `apllication/pl.edu.agh.servicetracker`, Data: `{"item_id":1}`

* Record1 - Android application record - `pl.edu.agh.servicetracker`

NFC Tools: Add a record -> Application -> `pl.edu.agh.servicetracker`


# QR Codes:

## format:
```
https://play.google.com/store/apps/details?id=pl.edu.agh.servicetracker item_id:1
```

