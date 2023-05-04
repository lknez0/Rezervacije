import { EditIcon, ViewIcon } from "@chakra-ui/icons"
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
  Button
} from "@chakra-ui/react"

export default function Dashboard() {
  const rezervacije = [
      {
        "id":1,
        "nazivObjekta":"Boogie Lab Zagreb",
        "adresaObjekta":"Ulica kneza Borne 26, Zagreb",
        "imeGosta":"Ivo Ivić",
        "brojMobitelaGosta":"0915882463",
        "vrijemePocetka":"19:00",
        "vrijemeZavrsetka":"22:00",
        "datumRezervacije":"24.07.2023",
        "danUTjednu":"Ponedjeljak",
        "brojStolica":4,
        "pozicijaStola":"Terasa"
      },
      {
        "id":2,
        "nazivObjekta":"Leggiero",
        "adresaObjekta":"Maksimirsko naselje IV 25, Zagreb",
        "imeGosta":"Marko Marić",
        "brojMobitelaGosta":"0998882425",
        "vrijemePocetka":"14:00",
        "vrijemeZavrsetka":"15:00",
        "datumRezervacije":"15.09.2023",
        "danUTjednu":"Srijeda",
        "brojStolica":2,
        "pozicijaStola":"Šank"
      },
      {
        "id":3,
        "nazivObjekta":"The Beertija",
        "adresaObjekta":"Ulica Pavla Hatza 16, Zagreb",
        "imeGosta":"Ana Anić",
        "brojMobitelaGosta":"0925653421",
        "vrijemePocetka":"11:30",
        "vrijemeZavrsetka":"13:00",
        "datumRezervacije":"03.08.2023",
        "danUTjednu":"Subota",
        "brojStolica":5,
        "pozicijaStola":"Separe"
      },
      {
        "id":4,
        "nazivObjekta":"Boogie Lab Zagreb",
        "adresaObjekta":"Ulica kneza Borne 26, Zagreb",
        "imeGosta":"Pero Perić",
        "brojMobitelaGosta":"0924771414",
        "vrijemePocetka":"18:00",
        "vrijemeZavrsetka":"20:00",
        "datumRezervacije":"30.11.2023",
        "danUTjednu":"Utorak",
        "brojStolica":2,
        "pozicijaStola":"Uz prozor"
      },
      {
        "id":5,
        "nazivObjekta":"The Beertija",
        "adresaObjekta":"Ulica Pavla Hatza 16, Zagreb",
        "imeGosta":"Marko Marić",
        "brojMobitelaGosta":"0998882425",
        "vrijemePocetka":"20:00",
        "vrijemeZavrsetka":"23:00",
        "datumRezervacije":"13.07.2023",
        "danUTjednu":"Petak",
        "brojStolica":3,
        "pozicijaStola":"Terasa"
      }
  ]

  return (
    <SimpleGrid spacing={10} minChildWidth={300}>
      {rezervacije && rezervacije.map(rezervacija => (
        <Card key={rezervacija.id} borderTop="8px" borderColor="purple.400" bg="white">

          <CardHeader color="gray.700">
            <Flex gap={5}>
              <Box w="50px" h="50px">
                <Text>REZ</Text>
              </Box>
              <Box>
                <Heading as="h3" size="sm">{rezervacija.nazivObjekta}</Heading>
                <Text>by {rezervacija.imeGosta}</Text>
              </Box>
            </Flex>
          </CardHeader>

          <CardBody color="gray.500">
            <Text>{rezervacija.adresaObjekta}</Text>
            <Text>{rezervacija.danUTjednu}, {rezervacija.datumRezervacije}</Text>
            <Text>{rezervacija.vrijemePocetka} - {rezervacija.vrijemeZavrsetka}</Text>
            <Text>{rezervacija.pozicijaStola} za {rezervacija.brojStolica} osobe</Text>
          </CardBody>

          <Divider borderColor="gray.200" />

          <CardFooter>
            <Button variant="ghost" leftIcon={<EditIcon />}>Uredi</Button>
          </CardFooter>

        </Card>
      ))}
    </SimpleGrid>
  )

}