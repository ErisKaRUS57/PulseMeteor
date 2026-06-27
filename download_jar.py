import urllib.request, os
url = "https://raw.githubusercontent.com/gradle/gradle/v8.10.0/gradle/wrapper/gradle-wrapper.jar"
path = "gradle/wrapper/gradle-wrapper.jar"
urllib.request.urlretrieve(url, path)
size = os.path.getsize(path)
print("Downloaded", size, "bytes")
if size < 10000:
    with open(path, "r") as f:
        print("Content:", f.read(200))
