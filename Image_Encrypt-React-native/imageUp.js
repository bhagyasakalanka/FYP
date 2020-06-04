import * as React from "react";
import { Button, Image, View, Text } from "react-native";
import * as ImagePicker from "expo-image-picker";
import Constants from "expo-constants";
import * as Permissions from "expo-permissions";

import CryptoJS from "crypto-js";

export default class ImagePickerExample extends React.Component {
  state = {
    image: null,
    encrypt: null,
    encryptingTime: 0,
    decryptingTime: 0,
  };

  render() {
    let { image, encryptingTime, decryptingTime } = this.state;

    return (
      <View
        style={{
          flex: 1,
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Button
          title="Pick an image from camera roll"
          onPress={this._pickImage}
        />
        {image && (
          <View>
            <Image
              source={{ uri: "data:image/jpeg;base64," + image }}
              style={{ width: 200, height: 200 }}
            />
            <Button title="decrypt image " onPress={this.decryptImg} />
          </View>
        )}
        <Text>encrypting time: {encryptingTime} ms</Text>
        <Text>decrypting time: {decryptingTime} ms</Text>
      </View>
    );
  }

  componentDidMount() {
    this.getPermissionAsync();
  }

  getPermissionAsync = async () => {
    if (Constants.platform.ios) {
      const { status } = await Permissions.askAsync(Permissions.CAMERA_ROLL);
      if (status !== "granted") {
        alert("Sorry, we need camera roll permissions to make this work!");
      }
    }
  };

  _pickImage = async () => {
    try {
      let result = await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.All,
        quality: 1,
        base64: true,
      });

      if (!result.cancelled) {
        // Encrypt
        console.log("start encrypting ", result.base64.length);
        var ed = new Date();
        var et = ed.getTime();
        var ciphertext = CryptoJS.AES.encrypt(
          result.base64,
          "secret key 123"
        ).toString();
        console.log("end encrypting ", ciphertext.length);
        var edn = new Date();
        this.setState({
          image: result.base64,
          encrypt: ciphertext,
          encryptingTime: edn.getTime() - et,
        });
      }

      //   console.log(result);
    } catch (E) {
      console.log(E);
    }
  };
  decryptImg = () => {
    var dd = new Date();
    var dt = dd.getTime();
    // Decrypt
    console.log("start decrypting");
    var bytes = CryptoJS.AES.decrypt(this.state.encrypt, "secret key 123");
    var originalText = bytes.toString(CryptoJS.enc.Utf8);
    console.log("end decrypting ", originalText.length);
    var ddn = new Date();
    this.setState({ decryptingTime: ddn.getTime() - dt });
  };
}
