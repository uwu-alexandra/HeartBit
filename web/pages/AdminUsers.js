function showAuthenticatedElements() {
  const elements = document.querySelectorAll('.authenticated');
  elements.forEach((element) => {
    element.style.display = 'block';
  });
}

// Helper function to hide authenticated elements
function hideAuthenticatedElements() {
  const elements = document.querySelectorAll('.authenticated');
  elements.forEach((element) => {
    element.style.display = 'none';
  });
}
hideAuthenticatedElements();
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref, onValue, get, set } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, signOut, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

const firebaseConfig = {
  // ...
  apiKey: "AIzaSyDHsp8a_NU7F8J-Gr5DaaWsoPUN79JO_ZY",

  authDomain: "proiectip-f2b57.firebaseapp.com",

  databaseURL: "https://proiectip-f2b57-default-rtdb.firebaseio.com",

  projectId: "proiectip-f2b57",

  storageBucket: "proiectip-f2b57.appspot.com",

  messagingSenderId: "134775428923",

  appId: "1:134775428923:web:a3542cd019c1e515c4891e",

  measurementId: "G-EGZ06QWFGX"
};
//var admin = require("firebase-admin");


//var serviceAccount = get("proiectip-f2b57-firebase-adminsdk-4il8d-b7ef4f7b87.json");


// admin.initializeApp({

//   credential: admin.credential.cert(serviceAccount),

//   databaseURL: "https://proiectip-f2b57-default-rtdb.firebaseio.com"

// });

// Initialize Firebase
const app = initializeApp(firebaseConfig);
// Get a reference to the database
const database = getDatabase(app);
const usersRef = ref(database, 'Users/');
const auth = getAuth();
onAuthStateChanged(auth, (user) => {
  const urlParams = new URLSearchParams(window.location.search);
  const uidParam = urlParams.get('uid');

  if (user && user.uid === uidParam) {
    let uid = user.uid;
    const userTypeRef = ref(database, `Users/${uid}/userType`);
    onValue(userTypeRef, (snapshot) => {
      const userType = snapshot.val();
      if (userType === 'admin') {
        showAuthenticatedElements();
      } else {
        window.location.href = 'index.html'; // Redirect to index.html if user is not admin
      }
    });
  } else {
    window.location.href = 'index.html'; // Redirect to index.html if no user is logged in or uid does not match
  }
});
var logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
  e.preventDefault();
  console.log('in eventlistener');
  signOut(auth).then(() => {
    alert('Signed out successfully');
    document.location = 'index.html';
  }).catch((error) => {
    console.log('Not logged in');
  });
});

// Retrieve data
const usersDiv = document.querySelector('#users');
onValue(usersRef, (snapshot) => {
  const users = snapshot.val();
  usersDiv.innerHTML = '';
  for (let userId in users) {
    const user = users[userId];
    const card = createCard(user, userId);
    usersDiv.appendChild(card);
  }
});
function createCard(user, userId) {
  //const toDelete = verifyUser(userId);
  const card = document.createElement('div');
  card.classList.add('card');

  card.innerHTML = `
    <img src="../images/person.png" style="max-width:40%;max-height:40%;object-fit:fill;">
    <p data-field="cnp">CNP: ${user.cnp}</p>
    <p data-field="nume">Nume: ${user.nume}</p>
    <p data-field="email">Email: ${user.email}</p>
    <p data-field="id">ID: ${user.id}</p>
    <p data-field="prenume">Prenume: ${user.prenume}</p>
    <p data-field="userType">User Type: ${user.userType}</p>
    <button class="button edit" data-userid="${userId}">Edit</button>
    <div class="edit-form" style="display: none;">
    <input type="text" class="edit-cnp" value="${user.cnp}"><br>
      <input type="text" class="edit-name" value="${user.nume}">
      <input type="text" class="edit-id" value="${user.id}">
      <input type="text" class="edit-prenume" value="${user.prenume}">
      <input type="text" class="edit-userType" value="${user.userType}"><br>
      <button class="button save" data-userid="${userId}">Save</button>
      <button class="button close" data-userid="${userId}">Close</button>
    </div>
  `;

 
  const editButton = card.querySelector('.button.edit');
 
  editButton.addEventListener('click', () => {
    toggleEditForm(userId);
  });

  const saveButton = card.querySelector('.button.save');
  const closeButton = card.querySelector('.button.close');
  saveButton.addEventListener('click', () => {
    saveUserData(userId);
  });
  closeButton.addEventListener('click', () => {
    toggleEditForm(userId);
  });

  return card;
}

function toggleEditForm(userId) {
  const card = document.querySelector(`.card [data-userid="${userId}"]`).closest('.card');
  const editForm = card.querySelector('.edit-form');
  editForm.style.display = editForm.style.display === 'none' ? 'block' : 'none';
}
function saveUserData(userId) {
  const card = document.querySelector(`.card [data-userid="${userId}"]`).closest('.card');
  const newName = card.querySelector('.edit-name').value;
  const newId = card.querySelector('.edit-id').value;
  const newCnp = card.querySelector('.edit-cnp').value;
  const newPrenume = card.querySelector('.edit-prenume').value;
  const newUserType = card.querySelector('.edit-userType').value;

  const userRef = ref(database, `Users/${userId}`);
  try {
    get(userRef).then((snapshot) => {
      const existingData = snapshot.val();
      const updatedData = {
        ...existingData,
        nume: newName || existingData.nume,
        id: newId || existingData.id,
        cnp: newCnp || existingData.cnp,
        prenume: newPrenume || existingData.prenume,
        userType: newUserType || existingData.userType
      };

      set(userRef, updatedData, (error) => {
        if (error) {
          console.log('Error saving user data:', error);
        } else {
          toggleEditForm(userId);
          const nameElement = card.querySelector('p[data-field="nume"]');
          const idElement = card.querySelector('p[data-field="id"]');
          const cnpElement = card.querySelector('p[data-field="cnp"]');
          const prenumeElement = card.querySelector('p[data-field="prenume"]');
          const userTypeElement = card.querySelector('p[data-field="userType"]');
          nameElement.textContent = `Name: ${updatedData.nume}`;
          idElement.textContent = `ID: ${updatedData.id}`;
          cnpElement.textContent = `CNP: ${updatedData.cnp}`;
          prenumeElement.textContent = `Prenume: ${updatedData.prenume}`;
          userTypeElement.textContent = `User Type: ${updatedData.userType}`;
        }
      });
    });
  } catch (error) {
    console.log('Error saving user data:', error);
  }
}

const searchBar = document.getElementById('searchBar');

searchBar.addEventListener('input', () => {
  const query = searchBar.value.toLowerCase();
  usersDiv.innerHTML = ''; // Clear previous results
  onValue(usersRef, (snapshot) => {
    const users = snapshot.val();
    for (let userId in users) {
      const user = users[userId];
      if ((user.nume.toLowerCase().includes(query) || user.prenume.toLowerCase().includes(query)) || (user.nume.toLowerCase() + ' ' + user.prenume.toLowerCase()).includes(query)) {
        const card = createCard(user, userId);
        usersDiv.appendChild(card);
      }
    }
  });
});