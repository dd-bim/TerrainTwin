import React from 'react';
import { ThemeProvider } from '@mui/material';
import './App.css';
import theme from './theme';
import AppTitle from './components/AppTitle';
import DatacatPanel from './components/DatacatPanel';
import TTObjectPanel from './components/TTObjectPanel';
import TTRelationsPanel from './components/TTRelationsPanel';
import RepositoryInput from './components/RepositoryInput';

function App() {
  const [repository, setRepository] = React.useState(null);
  return (
    <div className="App">
      <header className="App-header">
        <ThemeProvider theme={theme}>

          <AppTitle>
          </AppTitle>
          <RepositoryInput repository={repository} setRepository={setRepository}>
          </RepositoryInput>
          <DatacatPanel repository={repository}>
          </DatacatPanel>
          <TTObjectPanel repository={repository}>
          </TTObjectPanel>
          <TTRelationsPanel repository={repository}>
          </TTRelationsPanel>
        </ThemeProvider>
      </header>
    </div>
  );
}

export default App;
