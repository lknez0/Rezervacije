import { DeleteIcon, EditIcon, ViewIcon } from "@chakra-ui/icons"
import { 
  Box, 
  SimpleGrid,
  Text,
  Flex,
  Heading,
  Card, 
  CardHeader,
  CardBody,
  CardFooter,
  Divider,
  HStack,
  Button,
  useToast
} from "@chakra-ui/react"
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface Rezervacija {
  id: number,
  nazivObjekta:string,
  adresaObjekta:string,
  imeGosta:string,
  brojMobitelaGosta:string,
  vrijemePocetka:string,
  vrijemeZavrsetka:string,
  datumRezervacije:string,
  danUTjednu:string,
  brojOsoba:number,
  brojStolica:number,
  pozicijaStola:string
};

export default function Dashboard() {
  const toast = useToast();
  const navigate = useNavigate();
  const [rezervacije, setRezervacije] = useState<Rezervacija[]>([]);

  const fetchData =() => {
    axios.get('http://localhost:8081/rezervacije/all').then((response) => {
      setRezervacije(response.data);
      console.log(response.data);
    });
  }

  useEffect(() => {
    fetchData();
  }, []);

  const deleteReservation = (id: string | number) => {
    // AXIOS obrisi rezervaciju
    axios.delete('http://localhost:8081/rezervacije/' + id).then(res => {
      console.log(res);
      console.log(res.data);
    });

    // refreshaj listu
    fetchData();

    toast({
      title: 'Obrisana rezervacija',
      description: "Rezervacija koju ste odabrali je obrisana",
      status: 'success',
      duration: 9000,
      isClosable: true
    });
  };

  const updateReservation = (id: number) => {
    // navigacija - otvori formu
    navigate('/update/' + id, { state: { id: id } })
    
  };

  return (
    <SimpleGrid spacing={10} minChildWidth={300}>
      {rezervacije && rezervacije.map(rez => (
        <Card key={rez.id} borderTop="8px" borderColor="purple.400" bg="white">

          <CardHeader color="gray.700">
            <Flex gap={5}>
              <Box w="50px" h="50px">
                <Text>REZ</Text>
              </Box>
              <Box>
                <Heading as="h3" size="sm">{rez.nazivObjekta}</Heading>
                <Text>by {rez.imeGosta}</Text>
              </Box>
            </Flex>
          </CardHeader>

          <CardBody color="gray.500">
            <Text>{rez.adresaObjekta}</Text>
            <Text>{rez.danUTjednu}, {rez.datumRezervacije}</Text>
            <Text>{rez.vrijemePocetka} - {rez.vrijemeZavrsetka}</Text>
            <Text>{rez.pozicijaStola} za {rez.brojStolica} osobe</Text>
          </CardBody>

          <Divider borderColor="gray.200" />

          <CardFooter>
          <HStack>
              <Button variant="ghost" leftIcon={<DeleteIcon />} onClick={() => {deleteReservation(rez.id)}}>Obriši</Button>
              <Button variant="ghost" leftIcon={<EditIcon />} onClick={() => {updateReservation(rez.id)}}>Uredi</Button>
            </HStack>
          </CardFooter>

        </Card>
      ))}
    </SimpleGrid>
  )

}