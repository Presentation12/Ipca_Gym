using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using LayerDAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace LayerBLL.Logics
{
    public class MarcacaoLogic
    {
        public static async Task<Response> GetMarcacoesLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Marcacao> marcacaoList = await LayerDAL.MarcacaoService.GetMarcacoesService(sqlDataSource);
            if (marcacaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Sucesso na obtenção dos dados";
                response.Data = new JsonResult(marcacaoList);
            }
            return response;
        }
    }
}
