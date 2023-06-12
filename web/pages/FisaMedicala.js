
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
import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js';
import { getDatabase, ref, onValue, limitToLast, get, query } from 'https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js';
import { getAuth, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

const firebaseConfig = {
  apiKey: "AIzaSyDHsp8a_NU7F8J-Gr5DaaWsoPUN79JO_ZY",

  authDomain: "proiectip-f2b57.firebaseapp.com",

  databaseURL: "https://proiectip-f2b57-default-rtdb.firebaseio.com",

  projectId: "proiectip-f2b57",

  storageBucket: "proiectip-f2b57.appspot.com",

  messagingSenderId: "134775428923",

  appId: "1:134775428923:web:a3542cd019c1e515c4891e",

  measurementId: "G-EGZ06QWFGX"
};

initializeApp(firebaseConfig);
const database = getDatabase();


const urlParams = new URLSearchParams(window.location.search);
const key = urlParams.get('uid');
const ekgRef = ref(database, `/path/to/Senzori/${key}/EKG/`);
const temperatureRef = ref(database, `/path/to/Senzori/${key}/TEMP/`);
const pulseRef = ref(database, `/path/to/Senzori/${key}/PULS/`);
const umdRef = ref(database, `/path/to/Senzori/${key}/UMD/`);
const temperatureTable = document.getElementById('temperatureTable');

const auth = getAuth();


let currentUserType;
let userType;
const usrRef = ref(database, 'Users');
let currentUser = '';
let userData = '';
let users = ''; let uid = '';
onAuthStateChanged(auth, (user) => {
  const urlParams = new URLSearchParams(window.location.search);
  const uidParam = urlParams.get('uid');

  if (!user && (uidParam && user.uid !== uidParam)) {
    window.location.href = 'index.html'; // Redirect to index.html if no user is logged in or uid does not match
  } else {
    const userTypeRef = ref(database, `Users/${user.uid}/userType`);
    onValue(userTypeRef, (snapshot) => {
      const userType = snapshot.val();
      if (userType === 'medic' || (uidParam && user.uid === uidParam)) {
        showAuthenticatedElements();
      } else {
        window.location.href = 'index.html'; // Redirect to index.html if user is not of type 'medic'
      }
    });
  }
});
onValue(usrRef, (snapshot) => {
  users = snapshot.val();
  currentUser = auth.currentUser;
  userData = users[currentUser.uid];
  uid = currentUser.uid;
  console.log(currentUser.uid);
});

onValue(usrRef, (snapshot) => {
  snapshot.forEach((childSnapshot) => {
    if (childSnapshot.key === uid) {
      currentUserType = childSnapshot.val().userType;
      console.log(currentUserType);
    }
  });
});
let nume = '';
console.log(currentUserType);
const user = document.getElementById('user');
onValue(usrRef, (snapshot) => {
  snapshot.forEach((childSnapshot) => {
    if (childSnapshot.key === key) {
      userType = childSnapshot.val().userType;
      nume = `${childSnapshot.val().nume} ${childSnapshot.val().prenume}`;
      let gen = 'x';
      if (childSnapshot.val().cnp.startsWith('1') || childSnapshot.val().cnp.startsWith('5'))
        gen = 'M';
      else
        gen = 'F';
      let an = '';
      if (childSnapshot.val().cnp.startsWith('1') || childSnapshot.val().cnp.startsWith('2'))
        an = '19' + childSnapshot.val().cnp.substring(1, 3);
      else
        an = '20' + childSnapshot.val().cnp.substring(1, 3);
      user.innerHTML = `
          <div class="container">
          <h1 class="info-heading">Nume: ${childSnapshot.val().nume} ${childSnapshot.val().prenume}</h1>
          <h1 class="info-heading">Email: ${childSnapshot.val().email}</h1>
          <h1 class="info-heading">CNP: ${childSnapshot.val().cnp}</h1>
          <h1 class="info-heading">ID: ${childSnapshot.val().id}</h1>
          <h1 class="info-heading">Data nasterii: ${an}/${childSnapshot.val().cnp.substring(3, 5)}/${childSnapshot.val().cnp.substring(5, 7)}</h1>
          <h1 class="info-heading">Gen: ${gen}</h1>
        </div>
          `
    }
  });
});
let lastNEntriesQuery = query(ekgRef, limitToLast(50));

onValue(lastNEntriesQuery, (snapshot) => {
  const pulseTable = document.getElementById('pulseTable');
  pulseTable.innerHTML = '';
  pulseTable.innerHTML = '<h2>Puls</h2><table class="table"><tr><th>Data</th><th>Puls</th><th>Status</th></tr></table>';
  const pulseTableBody = pulseTable.querySelector('table');
  const pulseData = [];
  snapshot.forEach((childSnapshot) => {
    const pulse = Number(childSnapshot.val().Valoare);
    const timestamp = childSnapshot.val().Time_stamp;
    const status = (pulse >= 60 && pulse <= 100) ? 'DA' : 'NU';
    const row = `<tr><td>${timestamp}</td><td>${pulse}</td><td>${status}</td></tr>`;
    pulseTableBody.innerHTML += row;

    pulseData.push({ timestamp, pulse });
  });


  pulseData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

  const last50Entries = pulseData.slice(-50); // Get the last 50 entries

  google.charts.load('current', { packages: ['corechart'] });
  google.charts.setOnLoadCallback(() => generateLineGraph1(last50Entries));
});

function generateLineGraph1(data) {
  const dataTable = new google.visualization.DataTable();
  dataTable.addColumn('string', 'Data');
  dataTable.addColumn('number', 'EKG');

  data.forEach((item) => {
    dataTable.addRow([item.timestamp, item.pulse]);
  });

  const options = {
    title: 'EKG',
    curveType: 'function',
    legend: { position: 'bottom' },
    hAxis: { title: 'Data' },
    vAxis: { title: 'EKG' },
    width: 1200,
    height: 600,
  };

  const chart = new google.visualization.LineChart(document.getElementById('ekgChart'));
  chart.draw(dataTable, options);

}
const tempData = [];
try {

  const lastNEntriesQuery = query(temperatureRef, limitToLast(50));

  onValue(lastNEntriesQuery, (snapshot) => {
    temperatureTable.innerHTML = '';
    temperatureTable.innerHTML = '<h2>Temperatura</h2><table class="table"><tr><th>Data</th><th>Temperatura</th><th>Status</th></tr></table>';
    const temperatureTableBody = temperatureTable.querySelector('table');
    snapshot.forEach((childSnapshot) => {
      const temperature = childSnapshot.val().Valoare;
      const timestamp = childSnapshot.val().Time_stamp;
      const status = (temperature >= 36.1 && temperature <= 37.2) ? 'DA' : 'NU';
      const row = `<tr><td>${timestamp}</td><td>${temperature}</td><td>${status}</td></tr>`;
      temperatureTableBody.innerHTML += row;
      tempData.push({ timestamp, temperature });
    });
    tempData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

    const last50Entries = tempData.slice(-50); // Get the last 50 entries

    google.charts.load('current', { packages: ['corechart'] });
    google.charts.setOnLoadCallback(() => generateLineGraph2(last50Entries));
  });
  function generateLineGraph2(data) {
    const dataTable = new google.visualization.DataTable();
    dataTable.addColumn('string', 'Data');
    dataTable.addColumn('number', 'Temperatura');

    data.forEach((item) => {
      dataTable.addRow([item.timestamp, item.temperature]);
    });

    const options = {
      title: 'Temperatura',
      curveType: 'function',
      legend: { position: 'bottom' },
      hAxis: { title: 'Data' },
      vAxis: { title: 'Temperatura' },
      width: 1200,
      height: 600,
    };

    const chart = new google.visualization.LineChart(document.getElementById('tempChart'));
    chart.draw(dataTable, options);
  }

}
catch { console.log('error'); }

lastNEntriesQuery = query(umdRef, limitToLast(50));

onValue(lastNEntriesQuery, (snapshot) => {

  const pulseData = [];
  snapshot.forEach((childSnapshot) => {
    const pulse = Number(childSnapshot.val().Valoare);
    const timestamp = childSnapshot.val().Time_stamp;
    pulseData.push({ timestamp, pulse });
  });


  pulseData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

  const last50Entries = pulseData.slice(-50); // Get the last 50 entries

  google.charts.load('current', { packages: ['corechart'] });
  google.charts.setOnLoadCallback(() => generateLineGraph3(last50Entries));
});

function generateLineGraph3(data) {
  const dataTable = new google.visualization.DataTable();
  dataTable.addColumn('string', 'Data');
  dataTable.addColumn('number', 'Umiditate');

  data.forEach((item) => {
    dataTable.addRow([item.timestamp, item.pulse]);
  });

  const options = {
    title: 'Umiditate',
    curveType: 'function',
    legend: { position: 'bottom' },
    hAxis: { title: 'Data' },
    vAxis: { title: 'Umiditate' },
    width: 1200,
    height: 600,

  };

  const chart = new google.visualization.LineChart(document.getElementById('humidChart'));
  chart.draw(dataTable, options);
}
lastNEntriesQuery = query(pulseRef, limitToLast(50));

onValue(lastNEntriesQuery, (snapshot) => {

  const pulseData = [];
  snapshot.forEach((childSnapshot) => {
    const pulse = Number(childSnapshot.val().Valoare);
    const timestamp = childSnapshot.val().Time_stamp;
    pulseData.push({ timestamp, pulse });
  });


  pulseData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

  const last50Entries = pulseData.slice(-50); // Get the last 50 entries

  google.charts.load('current', { packages: ['corechart'] });
  google.charts.setOnLoadCallback(() => generateLineGraph4(last50Entries));
});

function generateLineGraph4(data) {
  const dataTable = new google.visualization.DataTable();
  dataTable.addColumn('string', 'Data');
  dataTable.addColumn('number', 'Puls');

  data.forEach((item) => {
    dataTable.addRow([item.timestamp, item.pulse]);
  });

  const options = {
    title: 'Puls',
    curveType: 'function',
    legend: { position: 'bottom' },
    hAxis: { title: 'Data' },
    vAxis: { title: 'Puls' },
    width: 1200,
    height: 600,
  };

  const chart = new google.visualization.LineChart(document.getElementById('pulseChart'));
  chart.draw(dataTable, options);
}

var logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
  e.preventDefault();
  console.log('in eventlistener');
  signOut(auth).then(() => {
    alert('Signed out successfully');
    document.location = 'index.html';
  }).catch(() => {
    console.log('Not logged in');
  });
});
const profil = document.getElementById('profil');
profil.addEventListener('click', () => {
  window.location.href = `Profil.html?uid=${key}`;
});
const rec = document.getElementById('rec');
rec.addEventListener('click', () => {
  if (currentUserType === 'medic')
    window.location.href = `RecomandareMedic.html?uid=${key}`;
  else
    window.location.href = `RecomandarePacient.html?uid=${key}`;
});
const hist = document.getElementById('hist');
hist.addEventListener('click', () => {
  window.location.href = `Istoric.html?uid=${key}`;
});
if (currentUserType === 'pacient') {
  document.getElementById('v5_47030').innerHTML = 'Profil';
}
else {
  document.getElementById('v5_47030').innerHTML = 'Pacienti';
}
profil.addEventListener('click', () => {
  if (currentUserType === 'pacient') {
    window.location.href = `Profil.html?uid=${uid}`;
  }
  else {
    window.location.href = `Pacienti.html`;
  }
});
rec.addEventListener('click', () => {
  if (currentUserType === 'pacient') {
    window.location.href = `RecomandarePacient.html?uid=${uid}`;
  }
  else {
    window.location.href = `RecomandareMedic.html?uid=${uid}`;

  }
});

window.addEventListener('DOMContentLoaded', () => {
  // Initialize Firebase app
  const app = initializeApp(firebaseConfig);

  // Get a reference to the database
  const database = getDatabase();

  const saveDataAsPDF = () => {
    console.log('uid = ' + key);
    const dataPath = `path/to/Senzori/${key}`;

    // Retrieve data from the specified path
    const dataRef = ref(database, dataPath);
    onValue(dataRef, (snapshot) => {
      const sensorData = snapshot.val();
      const { jsPDF } = window.jspdf;

      // Create a new PDF document
      const doc = new jsPDF();

      // Set the content of the PDF document
      doc.text(nume, 10, 10);

      // Prepare the table data
      const tableData = [];
      for (const entryKey in sensorData) {
        if (sensorData.hasOwnProperty(entryKey)) {
          const entry = sensorData[entryKey];
          for (const objKey in entry) {
            if (entry.hasOwnProperty(objKey)) {
              const obj = entry[objKey];
              tableData.push(obj);
            }
          }
        }
      }

      // Set the table headers
      const tableHeaders = ['Alerta', 'Denumire', 'Identificator', 'Time_Stamp', 'Valoare'];

      // Generate the table using the autoTable plugin
      doc.autoTable({
        head: [tableHeaders],
        body: tableData.map(obj => Object.values(obj)),
        startY: 20,
        theme: 'grid',
      });

      // Save the PDF document
      doc.save('data.pdf');
    }, (error) => {
      console.log('Error retrieving data:', error);
    });
  };

  // Add event listener to the download as JSON button
  const downloadJSONButton = document.getElementById('download');
  downloadJSONButton.addEventListener('click', saveDataAsPDF);
});