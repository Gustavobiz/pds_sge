import {ComponentPreview, Previews} from '@react-buddy/ide-toolbox'
import {PaletteTree} from './palette'
import LoginUsuario from "../components/LoginUsuario";

const ComponentPreviews = () => {
    return (
        <Previews palette={<PaletteTree/>}>
            <ComponentPreview path="/LoginUsuario">
                <LoginUsuario/>
            </ComponentPreview>
        </Previews>
    )
}

export default ComponentPreviews