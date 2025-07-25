import React, { useState, useEffect } from 'react';
import { Button, Typography, Container, Box, Card, CardContent } from '@mui/material';
import axios from 'axios';
import './App.css';

function App() {
  const [backendStatus, setBackendStatus] = useState<string>('Checking...');
  const [isLoading, setIsLoading] = useState<boolean>(false);

  // Probar conexión con backend al cargar
  useEffect(() => {
    checkBackendHealth();
  }, []);

  const checkBackendHealth = async () => {
    setIsLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/Health');
      setBackendStatus(response.data);
    } catch (error) {
      setBackendStatus('Backend connection failed ❌');
      console.error('Error connecting to backend:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 4 }}>
        <Typography variant="h2" component="h1" gutterBottom align="center">
          OCI Language Analytics
        </Typography>
        
        <Card sx={{ mt: 3 }}>
          <CardContent>
            <Typography variant="h5" gutterBottom>
              🔌 Backend Connection Test
            </Typography>
            
            <Typography variant="body1" sx={{ mb: 2 }}>
              Status: {backendStatus}
            </Typography>
            
            <Button 
              variant="contained" 
              onClick={checkBackendHealth}
              disabled={isLoading}
            >
              {isLoading ? 'Testing...' : 'Test Connection'}
            </Button>
          </CardContent>
        </Card>
      </Box>
    </Container>
  );
}

export default App;