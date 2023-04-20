
// Import the functions you need from the SDKs you need

//import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, signInWithEmailAndPassword, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";


  // Import the functions you need from the SDKs you need

  import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";

  // TODO: Add SDKs for Firebase products that you want to use

  // https://firebase.google.com/docs/web/setup#available-libraries


  // Your web app's Firebase configuration

  const firebaseConfig = {

    apiKey: "AIzaSyCkwWdVl7CPF8lcuxmnFTtux4qeM0HDMSQ",

    authDomain: "iptemp2.firebaseapp.com",

    projectId: "iptemp2",

    storageBucket: "iptemp2.appspot.com",

    messagingSenderId: "1020706358572",

    appId: "1:1020706358572:web:58bde92bdf16f881009889"

  };


  // Initialize Firebase

const app = initializeApp(firebaseConfig);

const database = getDatabase(app);
const auth = getAuth();


var signinForm = document.querySelector('#signin');
signinForm.addEventListener('click', (e) => {
  var email = document.getElementById('email').toString();
var password = document.getElementById('password').toString();
  e.preventDefault();
  console.log('aici');
  signInWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      // Signed in 
      const user = userCredential.user;
      alert('Logged in successfully');
      console.log('ceva');
      // ...
    })
    .catch((error) => {
      const errorCode = error.code;
      const errorMessage = error.message;
      alert(errorMessage);
      console.log('Not good');
    });
});
console.log(email);
console.log(auth.currentUser);
onAuthStateChanged(auth, (user) => {
  if (user) {
    // User is signed in, see docs for a list of available properties
    const uid = user.uid;
    var email = user.email;
    console.log(email);
    var userType = user.email.toString().charAt(0);
    /*switch (userType) {
      case 'b':
        document.location = 'WelcomeAdmin.html';
        break;
      case 'c':
        document.location = 'WelcomeMedic.html';
        break;
      case 'd':
        document.location = 'WelcomePacient.html';
        break;
    }*/
    // ...
  } else {
    // User is signed out
    // ...
    console.log('Not signed in')
  }
});
